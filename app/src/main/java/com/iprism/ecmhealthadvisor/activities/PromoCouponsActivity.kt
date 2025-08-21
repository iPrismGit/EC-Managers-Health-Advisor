package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.PromoCouponsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityPromoCouponsBinding

class PromoCouponsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromoCouponsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPromoCouponsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupPromoCouponsAdapter()
        handleDoctorConsultationTestViewAllBtn()
        handleDiagnosticTestViewAllBtn()
        handleHealthCheckupViewAllBtn()
        handleSurgicalPackagesViewAllBtn()
    }

    private fun handleSurgicalPackagesViewAllBtn() {
        binding.surgicalPackagesViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SinglePromoCouponsActivity::class.java)
            intent.putExtra("name", "Surgical Packages")
            startActivity(intent)
        })
    }

    private fun handleHealthCheckupViewAllBtn() {
        binding.healthCheckupsViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SinglePromoCouponsActivity::class.java)
            intent.putExtra("name", "Health Check Ups")
            startActivity(intent)
        })
    }

    private fun handleDiagnosticTestViewAllBtn() {
        binding.diagnosticViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SinglePromoCouponsActivity::class.java)
            intent.putExtra("name", "Diagnostic Test")
            startActivity(intent)
        })
    }

    private fun handleDoctorConsultationTestViewAllBtn() {
        binding.docConsultationViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SinglePromoCouponsActivity::class.java)
            intent.putExtra("name", "Doctor Consultations")
            startActivity(intent)
        })

    }

    private fun setupPromoCouponsAdapter() {
        var promoCouponsAdapter = PromoCouponsAdapter(this)
        binding.docConsultationsRv.adapter = promoCouponsAdapter
        binding.diagnosticTestsRv.adapter = promoCouponsAdapter
        binding.healthCheckupsRv.adapter = promoCouponsAdapter
        binding.surgicalPackagesRv.adapter = promoCouponsAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager3 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.docConsultationsRv.layoutManager = linearLayoutManager
        binding.diagnosticTestsRv.layoutManager = linearLayoutManager1
        binding.healthCheckupsRv.layoutManager = linearLayoutManager2
        binding.surgicalPackagesRv.layoutManager = linearLayoutManager3
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}