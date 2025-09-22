package com.iprism.ecmhealthadvisor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalTieUpsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivitySingleHospitalTieupsBinding

class SingleHospitalTieupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleHospitalTieupsBinding
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySingleHospitalTieupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = intent.getStringExtra("name").toString()
        binding.nameTxt.text = name
        handleBack()
        setupInsuranceCompaniesAdapter()
    }

    private fun setupInsuranceCompaniesAdapter() {
        var hospitalTieUpsAdapter = HospitalTieUpsAdapter(this)
        var linearLayoutManager = GridLayoutManager(this, 4)
        binding.companiesRv.adapter = hospitalTieUpsAdapter
        binding.companiesRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}