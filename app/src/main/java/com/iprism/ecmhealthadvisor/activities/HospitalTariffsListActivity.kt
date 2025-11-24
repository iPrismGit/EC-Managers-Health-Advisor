package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.HospitalTariffsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalTariffsListBinding
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalTariffsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TariffCategory
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class HospitalTariffsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalTariffsListBinding
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalTariffsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.tariffCategoriesRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        handleBack()
        initViewModel()
        observeHospitalTariffCategoriesResponse()
        handleRefreshLo()
        var hospitalTariffsApiRequest = HospitalTariffsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            "",
            userDetails[User.MAIN_DATA_ID].toString(),
            1,
            userDetails[User.ID].toString(),
            "categories"
        )
        hospitalViewModel.fetchHospitalTariffs(hospitalTariffsApiRequest)
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                var hospitalTariffsApiRequest = HospitalTariffsApiRequest(
                    userDetails[User.AUTH_TOKEN].toString(),
                    "",
                    userDetails[User.MAIN_DATA_ID].toString(),
                    1,
                    userDetails[User.ID].toString(),
                    "categories"
                )
                hospitalViewModel.fetchHospitalTariffs(hospitalTariffsApiRequest)
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeHospitalTariffCategoriesResponse() {
        hospitalViewModel.hospitalTariffsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.noDataTxt.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.categories.isNotEmpty()) {
                        setupHospitalTariffsAdapter(result.data.categories)
                        binding.tariffCategoriesRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        binding.tariffCategoriesRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.tariffCategoriesRv.visibility = View.GONE
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

    private fun setupHospitalTariffsAdapter(categories: List<TariffCategory>) {
        var hospitalTariffsAdapter = HospitalTariffsAdapter(this, categories)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.tariffCategoriesRv.adapter = hospitalTariffsAdapter
        binding.tariffCategoriesRv.layoutManager = linearLayoutManager
        hospitalTariffsAdapter.setupListener(object : OnWhiteBoardClickListener {
            override fun onItemClick(boardId: String, name: String) {
                var intent = Intent(
                    this@HospitalTariffsListActivity,
                    HospitalTariffSingleListActivity::class.java
                )
                intent.putExtra("catId", boardId)
                intent.putExtra("name", name)
                startActivity(intent)
            }

        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}