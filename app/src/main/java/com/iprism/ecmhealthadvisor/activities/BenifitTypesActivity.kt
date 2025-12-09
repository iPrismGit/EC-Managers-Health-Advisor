package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.BenefitsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityBenifitTypesBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.interfaces.OnBenefitClickListener
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.BenefitsResponse
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.HealthAdvisorBenefitsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HealthAdvisorBenefitsRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HealthAdvisorBenefitsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class BenifitTypesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBenifitTypesBinding
    private var catId = ""
    private var name = ""
    private lateinit var user: User
    private var selectedIds =""
    private lateinit var userDetails: HashMap<String, String?>
    private lateinit var adviserBenefitsViewModel: HealthAdvisorBenefitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBenifitTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User(this)
        userDetails = user.getUserDetails()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        catId = intent.getStringExtra("catId").toString()
        name = intent.getStringExtra("name").toString()
        binding.titleTxt.text = name
        handleBack()
        initViewModel()
        observeHealthAdvisorSubCategoriesResponse()
        handleBookAppointmentBtn()
        observeBookingResponse()
        handleRefreshLo()
        var request = HealthAdvisorBenefitsApiRequest(userDetails[User.AUTH_TOKEN].toString(),
            catId, userDetails[User.MAIN_DATA_ID].toString(), "",
            userDetails[User.ID].toString(), "sub_categories")
        adviserBenefitsViewModel.fetchHealthAdvisorBenefitSubCategories(request)
    }

    private fun initViewModel() {
        val repository = HealthAdvisorBenefitsRepository()
        val factory = ViewModelFactory { HealthAdvisorBenefitsViewModel(repository) }
        adviserBenefitsViewModel = ViewModelProvider(this, factory)[HealthAdvisorBenefitsViewModel::class.java]
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                var request = HealthAdvisorBenefitsApiRequest(userDetails[User.AUTH_TOKEN].toString(),
                    catId, userDetails[User.MAIN_DATA_ID].toString(), "",
                    userDetails[User.ID].toString(), "sub_categories")
                adviserBenefitsViewModel.fetchHealthAdvisorBenefitSubCategories(request)
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun handleBookAppointmentBtn() {
        binding.bookAppointmentBtn.setOnClickListener { view ->
            if (selectedIds.isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please select at least one benefit..!")
            } else if (!binding.acceptCb.isChecked){
                ToastUtils.showErrorCustomToast(this, "Please accept terms and conditions..!")
            } else{
                var request = HealthAdvisorBenefitsApiRequest(userDetails[User.AUTH_TOKEN].toString(),
                    catId, userDetails[User.MAIN_DATA_ID].toString(), selectedIds,
                    userDetails[User.ID].toString(), "book")
                adviserBenefitsViewModel.healthAdvisorBenefitBooking(request)
            }
        }
    }

    private fun setupBenefitsAdapter(benefits : List<BenefitsResponse>) {
        var benefitsAdapter = BenefitsAdapter(this, benefits)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.benefitsRv.adapter = benefitsAdapter
        binding.benefitsRv.layoutManager = linearLayoutManager
        benefitsAdapter.setupListener(object : OnBenefitClickListener{
            override fun onItemClick(id: String, name: String) {
                Log.d("SelectedIds", id)
                selectedIds = id
            }
        })
    }

    private fun observeHealthAdvisorSubCategoriesResponse() {
        adviserBenefitsViewModel.benefitSubCategoriesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.isNotEmpty()){
                        setupBenefitsAdapter(result.data)
                        binding.benefitsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else{
                        binding.benefitsRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.benefitsRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observeBookingResponse() {
        adviserBenefitsViewModel.benefitsBookingResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Your Benefits has been booked ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.benefitsRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

}