package com.iprism.ecmhealthadvisor.activities

import SelectedFileAdapter
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmhealthadvisor.databinding.ActivityRefferAnAdmissionBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.FileItem
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.FileType
import com.iprism.ecmhealthadvisor.modals.toprequests.ReferAnAdmissionApiRequest
import com.iprism.ecmhealthadvisor.repositoris.TopRequestRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.TopRequestViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import java.util.regex.Pattern

class RefferAnAdmissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRefferAnAdmissionBinding
    private lateinit var offerLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private val REQUEST_CODE_IMAGES = 100
    private val REQUEST_CODE_PDFS = 101
    private val selectedFiles = mutableListOf<FileItem>()
    private lateinit var adapter: SelectedFileAdapter
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private lateinit var viewModel: TopRequestViewModel
    private var lat = ""
    private var lon = ""
    private var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRefferAnAdmissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User(this)
        userDetails = user.getUserDetails()
        initViewModel()
        observeReferAnAdmissionResponse()
        setupActivityResultLaunchers()
        setupSelectedFilesAdapter()
        setupFilePickers()
        handleBack()
        handleReferBtn()
        handleAddressLo()
    }

    private fun setupSelectedFilesAdapter() {
        adapter = SelectedFileAdapter(selectedFiles)
        binding.selectedFilesRv.layoutManager = GridLayoutManager(this, 3)
        binding.selectedFilesRv.adapter = adapter
    }

    private fun setupFilePickers() {
        binding.galleryIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            startActivityForResult(
                Intent.createChooser(intent, "Select Images"),
                REQUEST_CODE_IMAGES
            )
        }

        binding.fileIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            startActivityForResult(Intent.createChooser(intent, "Select PDFs"), REQUEST_CODE_PDFS)
        }
    }

    private fun initViewModel() {
        val repo = TopRequestRepository()
        val factory = ViewModelFactory { TopRequestViewModel(repo) }
        viewModel = ViewModelProvider(this, factory)[TopRequestViewModel::class.java]
    }

    private fun observeReferAnAdmissionResponse() {
        viewModel.referAdmissionResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.referNowBtn.isEnabled = false
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    val intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Refer Admission Request Sent")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.referNowBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun handleReferBtn() {
        binding.referNowBtn.setOnClickListener {

            when {
                getName().isEmpty() -> toast("Please Enter Patient Name..!")
                getAge().isEmpty() -> toast("Please Enter Patient Age..!")
                getAge().matches(Regex("0+")) -> toast("Patient Age Should not be Zero..!")
                getReason().isEmpty() -> toast("Please Enter Reason for Admission..!")
                getMobile().isEmpty() -> toast("Please Enter Patient Mobile Number..!")
                getMobile().length != 10 -> toast("Please Enter Valid Mobile Number..!")
                Pattern.matches(
                    "[0-5].*",
                    getMobile()
                ) -> toast("Please Enter Valid Mobile Number..!")

                getAddress().isEmpty() -> toast("Please Select Patient Address..!")
                else -> submitAdmissionRequest()
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { finish() }
    }

    private fun handleAddressLo() {
        binding.addressLo.setOnClickListener {
            if (hasLocationPermission()) {
                offerLauncher.launch(Intent(this, LocationActivity::class.java))
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun submitAdmissionRequest() {

        val base64List = selectedFiles.map { convertUriToBase64(it.uri) }
        val commaSeparatedString = base64List.joinToString(",")

        val request = ReferAnAdmissionApiRequest(
            getAge(),
            userDetails[User.AUTH_TOKEN].toString(),
            commaSeparatedString,
            lat,
            getAddress(),
            lon,
            userDetails[User.MAIN_DATA_ID].toString(),
            getMobile(),
            getName(),
            getReason(),
            userDetails[User.ID].toString()
        )

        viewModel.referAdmission(request)
        Log.d("ReferAdmissionRequest", request.toString())
    }

    private fun getName() = binding.nameTxt.text.toString().trim()
    private fun getAge() = binding.ageTxt.text.toString().trim()
    private fun getReason() = binding.reasonTxt.text.toString().trim()
    private fun getMobile() = binding.contactNumberTxt.text.toString().trim()
    private fun getAddress() = binding.addressTxt.text.toString().trim()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {

            val clipData = data.clipData
            val type = if (requestCode == REQUEST_CODE_IMAGES) FileType.IMAGE else FileType.PDF

            if (selectedFiles.isNotEmpty() && selectedFiles[0].type != type) {
                selectedFiles.clear()
            }

            val currentCount = selectedFiles.size

            if (clipData != null) {
                val newCount = clipData.itemCount
                if (currentCount + newCount > 10) return toast("You can select only 10 ${type.name.lowercase()}s.")

                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    selectedFiles.add(FileItem(uri, type))
                }
            } else {
                data.data?.let { uri ->
                    if (currentCount + 1 > 10) return toast("You can select only 10 ${type.name.lowercase()}s.")
                    selectedFiles.add(FileItem(uri, type))
                }
            }

            adapter.notifyDataSetChanged()
        }
    }

    private fun convertUriToBase64(uri: Uri): String {
        val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: byteArrayOf()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun setupActivityResultLaunchers() {
        offerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->
                        address = data.getStringExtra("address").orEmpty()
                        lat = data.getStringExtra("lat").orEmpty()
                        lon = data.getStringExtra("lon").orEmpty()

                        binding.addressTxt.text = address
                    }
                }
            }

        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

                if (granted) {
                    offerLauncher.launch(Intent(this, LocationActivity::class.java))
                } else {
                    toast("Location permission is required to continue")
                }
            }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    private fun toast(msg: String) {
        ToastUtils.showErrorCustomToast(this, msg)
    }

}