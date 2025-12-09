package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.AdvisorBenefitCategoriesAdapter
import com.iprism.ecmhealthadvisor.adapters.HospitalTariffsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityAdvisorBenefitsBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.interfaces.OnBenefitClickListener
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.BenefitsResponse
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.HealthAdvisorBenefitsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HealthAdvisorBenefitsRepository
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HealthAdvisorBenefitsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class AdvisorBenefitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdvisorBenefitsBinding
    private lateinit var benefitCategoriesAdapter : AdvisorBenefitCategoriesAdapter
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private lateinit var adviserBenefitsViewModel: HealthAdvisorBenefitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdvisorBenefitsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        user = User(this)
        userDetails = user.getUserDetails()
        handleBack()
        initViewModel()
        observeHealthAdvisorResponse()
        handleRefreshLo()
        var request = HealthAdvisorBenefitsApiRequest(userDetails[User.AUTH_TOKEN].toString(),
            "", userDetails[User.MAIN_DATA_ID].toString(), "",
            userDetails[User.ID].toString(), "categories")
        adviserBenefitsViewModel.fetchHealthAdvisorBenefitCategories(request)
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                var request = HealthAdvisorBenefitsApiRequest(userDetails[User.AUTH_TOKEN].toString(),
                    "", userDetails[User.MAIN_DATA_ID].toString(), "",
                    userDetails[User.ID].toString(), "categories")
                adviserBenefitsViewModel.fetchHealthAdvisorBenefitCategories(request)
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun initViewModel() {
        val repository = HealthAdvisorBenefitsRepository()
        val factory = ViewModelFactory { HealthAdvisorBenefitsViewModel(repository) }
        adviserBenefitsViewModel = ViewModelProvider(this, factory)[HealthAdvisorBenefitsViewModel::class.java]
    }

    private fun setupCategoriesAdapter(categories: List<BenefitsResponse>) {
        benefitCategoriesAdapter = AdvisorBenefitCategoriesAdapter(this, categories)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.benefitCategoriesRv.adapter = benefitCategoriesAdapter
        binding.benefitCategoriesRv.layoutManager = linearLayoutManager
        benefitCategoriesAdapter.setupListener(object : OnBenefitClickListener{
            override fun onItemClick(id: String, name: String) {
                var intent = Intent(this@AdvisorBenefitsActivity, BenifitTypesActivity::class.java)
                intent.putExtra("catId", id)
                intent.putExtra("name", name)
                startActivity(intent)
            }

        })
    }

    private fun observeHealthAdvisorResponse() {
        adviserBenefitsViewModel.benefitCategoriesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                   if (result.data.isNotEmpty()){
                       setupCategoriesAdapter(result.data)
                       binding.benefitCategoriesRv.visibility = View.VISIBLE
                       binding.noDataTxt.visibility = View.GONE
                   } else{
                       binding.benefitCategoriesRv.visibility = View.GONE
                       binding.noDataTxt.visibility = View.VISIBLE
                   }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.benefitCategoriesRv.visibility = View.GONE
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