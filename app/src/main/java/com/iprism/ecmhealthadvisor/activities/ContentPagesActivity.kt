package com.iprism.ecmhealthadvisor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmhealthadvisor.databinding.ActivityContentPagesBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.ContentPagesApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class ContentPagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentPagesBinding
    private var tag: String = ""
    private var name: String = ""
    private lateinit var hospitalViewModel: HospitalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContentPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tag = intent.getStringExtra("tag").toString()
        name = intent.getStringExtra("name").toString()
        binding.textView10.text = name
        handleBack()
        initViewModel()
        observeHospitalContentResponse()
        var contentPagesApiRequest = ContentPagesApiRequest(tag)
        hospitalViewModel.fetchContentPagesData(contentPagesApiRequest)
    }

    private fun observeHospitalContentResponse() {
        hospitalViewModel.contentPagesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.contentTxt.text = result.data.name
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
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
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}