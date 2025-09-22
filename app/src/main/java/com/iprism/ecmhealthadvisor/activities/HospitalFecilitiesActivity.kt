package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalFecilitiesAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalFecilitiesBinding

class HospitalFecilitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalFecilitiesBinding

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
        handleBack()
        setupFecilitiesAdapter()
    }

    private fun setupFecilitiesAdapter() {
        var fecilitiesAdapter = HospitalFecilitiesAdapter(this)
        binding.casualitiesRv.adapter = fecilitiesAdapter
        binding.criticalCareRv.adapter = fecilitiesAdapter
        binding.operationTheatersRv.adapter = fecilitiesAdapter
        binding.hospitalInfraRv.adapter = fecilitiesAdapter
        binding.diagnosticServicesRv.adapter = fecilitiesAdapter
        binding.cathLabsRv.adapter = fecilitiesAdapter
        binding.surgicalBranchesRv.adapter = fecilitiesAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager3 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager4 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager5 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager6 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.casualitiesRv.layoutManager = linearLayoutManager
        binding.criticalCareRv.layoutManager = linearLayoutManager1
        binding.operationTheatersRv.layoutManager = linearLayoutManager2
        binding.hospitalInfraRv.layoutManager = linearLayoutManager3
        binding.diagnosticServicesRv.layoutManager = linearLayoutManager4
        binding.cathLabsRv.layoutManager = linearLayoutManager5
        binding.surgicalBranchesRv.layoutManager = linearLayoutManager6
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}