package com.iprism.ecmhealthadvisor.activities

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
import com.iprism.ecmhealthadvisor.adapters.HospitalTariffSingleListAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalTariffSingleListBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalTariffsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Tariff
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class HospitalTariffSingleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalTariffSingleListBinding
    private var tariffsList = mutableListOf<Tariff>()
    private lateinit var tariffsAdapter: HospitalTariffSingleListAdapter
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var catId: String = ""
    private var name: String = ""
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalTariffSingleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        catId = intent.getStringExtra("catId").toString()
        name = intent.getStringExtra("name").toString()
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.singleTariffsRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        setupRecyclerView()
        initViewModel()
        observeHospitalDoctorsResponse()
        loadTariffs()
        handleRefreshLo()
        handleBack()
        binding.titleTxt.text = name
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun setupRecyclerView() {
        tariffsAdapter = HospitalTariffSingleListAdapter(this, tariffsList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.singleTariffsRv.apply {
            layoutManager = linearLayoutManager
            adapter = tariffsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.singleTariffsRv.canScrollVertically(-1)
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

    }

    private fun loadTariffs() {
        val request = HospitalTariffsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            catId,
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            "tariffs"
        )
        hospitalViewModel.fetchHospitalTariffs(request)
    }

    private fun refreshDoctors() {
        currentPage = 1
        isLastPage = false
        tariffsList.clear()
        tariffsAdapter.notifyDataSetChanged()
        val request = HospitalTariffsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            catId,
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            "tariffs"
        )
        hospitalViewModel.fetchHospitalTariffs(request)
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        loadMoreItems()
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshDoctors()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeHospitalDoctorsResponse() {
        hospitalViewModel.hospitalTariffsResponse.observe(this) { result ->


            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.tariffs
                    if (newBookings.isNotEmpty()) {
                        tariffsList.addAll(newBookings)
                        tariffsAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.singleTariffsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.singleTariffsRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.singleTariffsRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

}