package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalHodsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalHodsBinding
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.utils.ToastUtils


class HospitalHodsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalHodsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalHodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupHospitalHodsAdpter()
    }

    private fun setupHospitalHodsAdpter() {
        var hospitalHodsAdapter = HospitalHodsAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.hospitalHodsRv.adapter = hospitalHodsAdapter
        binding.hospitalHodsRv.layoutManager = linearLayoutManager
        hospitalHodsAdapter.setupListener(object : OnSingleItemClickListener{
            override fun onCallNowClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(this@HospitalHodsActivity, "Calling to the Hospital Hod!")
            }

            override fun onSmsClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(this@HospitalHodsActivity, "Messaging to the Hospital Hod!")
            }

            override fun onWhatsappClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(this@HospitalHodsActivity, "Navigate to Whatsapp!")
            }

        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}