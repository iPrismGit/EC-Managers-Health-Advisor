package com.iprism.ecmhealthadvisor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalTariffSingleListAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalTariffSingleListBinding

class HospitalTariffSingleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalTariffSingleListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalTariffSingleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupTariffsSingleListAdapter()
    }

    private fun setupTariffsSingleListAdapter() {
        var tariffSingleListAdapter = HospitalTariffSingleListAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.singleTariffsRv.adapter = tariffSingleListAdapter
        binding.singleTariffsRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}