package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.HospitalTariffsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityAdvisorBenefitsBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener

class AdvisorBenefitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdvisorBenefitsBinding

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
        handleBack()
        setupAdvisorBenefitsAdapter()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun setupAdvisorBenefitsAdapter() {
        var hospitalTariffsAdapter = HospitalTariffsAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.tariffsRv.adapter = hospitalTariffsAdapter
        binding.tariffsRv.layoutManager = linearLayoutManager
        hospitalTariffsAdapter.setupListener(object : OnWhiteBoardClickListener {
            override fun onItemClick(boardId: String) {
                var intent = Intent(this@AdvisorBenefitsActivity, BenifitTypesActivity::class.java)
                startActivity(intent)
            }

        })
    }
}