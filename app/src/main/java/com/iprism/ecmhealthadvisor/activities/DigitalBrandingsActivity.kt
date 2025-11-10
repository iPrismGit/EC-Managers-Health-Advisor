package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.DigitalPromosAdapter
import com.iprism.ecmhealthadvisor.adapters.HospitalTieUpsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityDigitalBrandingsBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromo
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromosApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.Constants
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DigitalBrandingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDigitalBrandingsBinding
    private lateinit var promosAdapter: DigitalPromosAdapter
    private var promosList = mutableListOf<DigitalPromo>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10

    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private var selectedUrl: String? = null
    private var selectedType: String? = null
    @RequiresApi(Build.VERSION_CODES.P)
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            handleImageSelection(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDigitalBrandingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.digitalPromosRv.canScrollVertically(-1)
        }
        setupRecyclerView()
        initViewModel()
        observeHospitalDoctorsResponse()
        loadPromos()
        handleRefreshLo()
        handleBack()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun handleImageSelection(imageUri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {

                val url = URL(selectedUrl)
                val connection = url.openConnection().apply {
                    connectTimeout = 10000
                    readTimeout = 10000
                }

                connection.connect()
                val inputStream = connection.getInputStream()
                val baseBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (baseBitmap == null) {
                    throw Exception("Failed to decode base image")
                }

                val source = ImageDecoder.createSource(contentResolver, imageUri)
                val selectedBitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                }

                val combined = overlayCircularImageBottomRight(baseBitmap, selectedBitmap)

                saveToTradeMarketingFolder(combined)

                withContext(Dispatchers.Main) {
                    ToastUtils.showSuccessCustomToast(
                        this@DigitalBrandingsActivity,
                        "Image processed successfully"
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    ToastUtils.showErrorCustomToast(
                        this@DigitalBrandingsActivity,
                        "Error processing image: ${e.localizedMessage ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    private fun overlayCircularImageBottomRight(base: Bitmap, overlay: Bitmap): Bitmap {
        val result = base.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        val overlaySize = (base.width * 0.20f).toInt()
        val scaledOverlay = Bitmap.createScaledBitmap(overlay, overlaySize, overlaySize, true)

        val circularOverlay = getCircularBitmap(scaledOverlay)

        val left = base.width - circularOverlay.width - 40f
        val top = base.height - circularOverlay.height - 40f

        canvas.drawBitmap(circularOverlay, left, top, null)

        return result
    }

    private suspend fun saveToTradeMarketingFolder(bitmap: Bitmap) {
        val folderName = "ECMHealthAdvisor"
        val downloads =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val folder = File(downloads, folderName)
        if (!folder.exists()) folder.mkdirs()

        val fileName = "promo_${System.currentTimeMillis()}.png"
        val file = File(folder, fileName)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            MediaScannerConnection.scanFile(this, arrayOf(file.absolutePath), null, null)
            withContext(Dispatchers.Main) {
                ToastUtils.showSuccessCustomToast(
                    this@DigitalBrandingsActivity,
                    "Saved to ${file.absolutePath}"
                )

            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                ToastUtils.showErrorCustomToast(
                    this@DigitalBrandingsActivity,
                    "Error saving file: ${e.message}"
                )

            }
        }

    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun setupRecyclerView() {
        promosAdapter = DigitalPromosAdapter(this, promosList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.digitalPromosRv.apply {
            layoutManager = linearLayoutManager
            adapter = promosAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.digitalPromosRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreItems()
                        }
                    }
                }
            })
        }
        promosAdapter.setupListener(object : DigitalPromosAdapter.OnFacilityOuterClickListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onItemClick(url: String, type: String) {
                selectedUrl = Constants.IMAGES_URL + url
                selectedType = type
                pickImageLauncher.launch("image/*")
            }

        })

    }

    private fun loadPromos() {
        val request = DigitalPromosApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchDigitalPromos(request)
    }

    private fun refreshData() {
        currentPage = 1
        isLastPage = false
        promosList.clear()
        promosAdapter.notifyDataSetChanged()
        val request = DigitalPromosApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchDigitalPromos(request)
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        loadPromos()
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshData()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeHospitalDoctorsResponse() {
        hospitalViewModel.digitalPromosResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.digital_promos
                    if (newBookings.isNotEmpty()) {
                        promosList.addAll(newBookings)
                        promosAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.digitalPromosRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.digitalPromosRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.digitalPromosRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

}