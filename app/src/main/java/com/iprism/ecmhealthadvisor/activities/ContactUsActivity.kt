package com.iprism.ecmhealthadvisor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.iprism.ecmhealthadvisor.databinding.ActivityContactUsBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleCallNowBtn()
        handleBack()
        handleSubmitBtn()
    }

    private fun handleSubmitBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Report Sent Successfully!")
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCallNowBtn() {
        binding.callNowBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Calling to the Support Team!")
        })
    }

}