package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.AllDigitalBrandingsAdapter
import com.iprism.ecmhealthadvisor.adapters.HospitalTieUpsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityViewAllDigitalBrandingsBinding

class ViewAllDigitalBrandingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAllDigitalBrandingsBinding
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewAllDigitalBrandingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        name = intent.getStringExtra("name").toString()
        binding.nameTxt.text = name
        handleBack()
        setupInsuranceCompaniesAdapter()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun setupInsuranceCompaniesAdapter() {
        var hospitalTieUpsAdapter = AllDigitalBrandingsAdapter()
        var linearLayoutManager = GridLayoutManager(this, 2)
        binding.companiesRv.adapter = hospitalTieUpsAdapter
        binding.companiesRv.layoutManager = linearLayoutManager
    }

}