package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.BenefitsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityBenifitTypesBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils

class BenifitTypesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBenifitTypesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBenifitTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupBenefitsAdapter()
        handleBookAppointmentBtn()
    }

    private fun handleBookAppointmentBtn() {
        binding.bookAppointmentBtn.setOnClickListener { view ->
            ToastUtils.showSuccessCustomToast(this, "Appointment Booked Successfully!")
            finish()
        }
    }

    private fun setupBenefitsAdapter() {
        var benefitsAdapter = BenefitsAdapter()
        var linearLayoutManager = LinearLayoutManager(this)
        binding.benefitsRv.adapter = benefitsAdapter
        binding.benefitsRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

}