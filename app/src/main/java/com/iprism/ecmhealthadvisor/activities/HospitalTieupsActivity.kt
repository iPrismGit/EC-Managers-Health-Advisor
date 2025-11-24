package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.HospitalTieUpsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalTieupsBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Tieup
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TieupsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class HospitalTieupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalTieupsBinding
    private lateinit var tieupsAdapter: HospitalTieUpsAdapter
    private var tieupsList = mutableListOf<Tieup>()
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
        binding = ActivityHospitalTieupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.tieupsRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        setupRecyclerView()
        initViewModel()
        observeHospitalTieupsResponse()
        loadPromos()
        handleRefreshLo()
    }

    private fun setupRecyclerView() {
        tieupsAdapter = HospitalTieUpsAdapter(this, tieupsList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.tieupsRv.apply {
            layoutManager = linearLayoutManager
            adapter = tieupsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled = !binding.tieupsRv.canScrollVertically(-1)
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
            tieupsAdapter.setupListener(object : HospitalTieUpsAdapter.OnFacilityOuterClickListener{
                override fun onItemClick(url: String, type: String) {
                    if (type.equals("video", true)){
                        val intent = Intent(this@HospitalTieupsActivity, VideoPlayActivity::class.java)
                        intent.putExtra("videoUrl", url)
                        startActivity(intent)
                    }
                }
            })
        }

    }

    private fun loadPromos() {
        val request = TieupsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchHospitalTieups(request)
    }

    private fun refreshData() {
        currentPage = 1
        isLastPage = false
        tieupsList.clear()
        tieupsAdapter.notifyDataSetChanged()
        val request = TieupsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchHospitalTieups(request)
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

    private fun observeHospitalTieupsResponse() {
        hospitalViewModel.tieupsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.tieups
                    if (newBookings.isNotEmpty()) {
                        tieupsList.addAll(newBookings)
                        tieupsAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.tieupsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.tieupsRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.tieupsRv.visibility = View.GONE
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