package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.adapters.MembersAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMobileContactMembersBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils

class MobileContactMembersActivity : AppCompatActivity() {

    private var tag = ""
    private lateinit var binding: ActivityMobileContactMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMobileContactMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tag = intent.getStringExtra("tag").toString()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.nameTxt.text = tag
        handleBack()
        setupMembersAdapter()
    }

    private fun setupMembersAdapter() {
        var membersAdapter = MembersAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.membersRv.adapter = membersAdapter
        binding.membersRv.layoutManager = linearLayoutManager
        membersAdapter.setupListener(object : OnSingleItemClickListener {
            override fun onCallNowClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(
                    this@MobileContactMembersActivity,
                    "Calling to the Member!"
                )
            }

            override fun onSmsClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(
                    this@MobileContactMembersActivity,
                    "Messaging to the Member!"
                )
            }

            override fun onWhatsappClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(
                    this@MobileContactMembersActivity,
                    "Navigate to Member Whatsapp"
                )
            }

        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }
}