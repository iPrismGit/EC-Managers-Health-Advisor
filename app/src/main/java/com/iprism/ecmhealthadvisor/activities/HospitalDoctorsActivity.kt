package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalHodsAdapter
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener

import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalDoctorsBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils

class HospitalDoctorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalDoctorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //setupHospitalDoctorsAdapter()
        handleBack()
    }

//    private fun setupHospitalDoctorsAdapter() {
//        var hospitalHodsAdapter = HospitalHodsAdapter(this)
//        var linearLayoutManager = LinearLayoutManager(this)
//        binding.hospitalDoctorsRv.adapter = hospitalHodsAdapter
//        binding.hospitalDoctorsRv.layoutManager = linearLayoutManager
//        hospitalHodsAdapter.setupListener(object : OnSingleItemClickListener {
//            override fun onCallNowClick(doctorId: String, mobile: String) {
//                ToastUtils.showSuccessCustomToast(
//                    this@HospitalDoctorsActivity,
//                    "Calling to the Hospital Doctor!"
//                )
//            }
//
//            override fun onSmsClick(doctorId: String, mobile: String) {
//                ToastUtils.showSuccessCustomToast(
//                    this@HospitalDoctorsActivity,
//                    "Messaging to the Hospital Doctor!"
//                )
//            }
//
//            override fun onWhatsappClick(doctorId: String, mobile: String) {
//                ToastUtils.showSuccessCustomToast(
//                    this@HospitalDoctorsActivity,
//                    "Navigate to Whatsapp!"
//                )
//            }
//
//        })
//    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}