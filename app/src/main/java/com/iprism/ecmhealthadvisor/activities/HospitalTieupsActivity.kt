package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalTieUpsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalTieupsBinding

class HospitalTieupsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalTieupsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalTieupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupAdapterInsuranceCompaniesAdapter()
        handleNonGIPSAViewAllBtn()
        handleGIPSAViewAllBtn()
        handleGovtViewAllBtn()
        handlePublicViewAllBtn()
        handlePrivateViewAllBtn()
        handleCertificationViewAllBtn()
    }

    private fun handleNonGIPSAViewAllBtn() {
        binding.nonGipsaViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SingleHospitalTieupsActivity::class.java)
            intent.putExtra("name", "Insurance Tie-ups (Non GIPSA)")
            startActivity(intent)
        })
    }

    private fun handleGIPSAViewAllBtn() {
        binding.gipsaViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SingleHospitalTieupsActivity::class.java)
            intent.putExtra("name", "Insurance Tie-ups (GIPSA)")
            startActivity(intent)
        })
    }

    private fun handleGovtViewAllBtn() {
        binding.governmentSectorViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SingleHospitalTieupsActivity::class.java)
            intent.putExtra("name", "Government Sector")
            startActivity(intent)
        })
    }

    private fun handlePublicViewAllBtn() {
        binding.publicSectorViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SingleHospitalTieupsActivity::class.java)
            intent.putExtra("name", "Public Sector")
            startActivity(intent)
        })
    }

    private fun handlePrivateViewAllBtn() {
        binding.privateSectorViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SingleHospitalTieupsActivity::class.java)
            intent.putExtra("name", "Private Sector")
            startActivity(intent)
        })
    }

    private fun handleCertificationViewAllBtn() {
        binding.certificationsViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, SingleHospitalTieupsActivity::class.java)
            intent.putExtra("name", "Certification Sector")
            startActivity(intent)
        })
    }

    private fun setupAdapterInsuranceCompaniesAdapter() {
        var hospitalTieUpsAdapter = HospitalTieUpsAdapter(this)
        binding.nonGipsaRv.adapter = hospitalTieUpsAdapter
        binding.gipsaRv.adapter = hospitalTieUpsAdapter
        binding.governmentSectorRv.adapter = hospitalTieUpsAdapter
        binding.publicSectorRv.adapter = hospitalTieUpsAdapter
        binding.privateSectorRv.adapter = hospitalTieUpsAdapter
        binding.certificationsRv.adapter = hospitalTieUpsAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager3 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager4 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager5 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.nonGipsaRv.layoutManager = linearLayoutManager
        binding.gipsaRv.layoutManager = linearLayoutManager1
        binding.governmentSectorRv.layoutManager = linearLayoutManager2
        binding.publicSectorRv.layoutManager = linearLayoutManager3
        binding.privateSectorRv.layoutManager = linearLayoutManager4
        binding.certificationsRv.layoutManager = linearLayoutManager5
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}