package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.HospitalFecilitiesAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalFecilitiesBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Facility
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalFacilitiesApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.Constants
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class HospitalFecilitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalFecilitiesBinding
    private lateinit var facilitiesAdapter: HospitalFecilitiesAdapter
    private var facilitiesList = mutableListOf<Facility>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalFecilitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.facilitiesRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        setupRecyclerView()
        initViewModel()
        observeHospitalFacilitiesResponse()
        loadPromos()
        handleRefreshLo()
        handleBack()
    }

    private fun setupRecyclerView() {
        facilitiesAdapter = HospitalFecilitiesAdapter(this, facilitiesList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.facilitiesRv.apply {
            layoutManager = linearLayoutManager
            adapter = facilitiesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled = !binding.facilitiesRv.canScrollVertically(-1)
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
        facilitiesAdapter.setupListener(object :
            HospitalFecilitiesAdapter.OnFacilityOuterClickListener {
            override fun onItemClick(url: String, type: String) {
                android.util.Log.d("urlAndType", url + ", " + type)
                if (type.equals("image", true)) {
                    var intent =
                        Intent(this@HospitalFecilitiesActivity, ViewPhotoActivity::class.java)
                    intent.putExtra("imageUrl", url)
                    startActivity(intent)
                } else if (type.equals("video", true)) {
                    var intent =
                        Intent(this@HospitalFecilitiesActivity, VideoPlayActivity::class.java)
                    intent.putExtra("videoUrl", Constants.IMAGES_URL + url)
                    startActivity(intent)
                }

            }

        })

    }

    private fun loadPromos() {
        val request = HospitalFacilitiesApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchHospitalFacilities(request)
    }

    private fun refreshData() {
        currentPage = 1
        isLastPage = false
        facilitiesList.clear()
        facilitiesAdapter.notifyDataSetChanged()
        val request = HospitalFacilitiesApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchHospitalFacilities(request)
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

    private fun observeHospitalFacilitiesResponse() {
        hospitalViewModel.facilitiesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.facilities
                    if (newBookings.isNotEmpty()) {
                        facilitiesList.addAll(newBookings)
                        facilitiesAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.facilitiesRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.facilitiesRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.facilitiesRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}