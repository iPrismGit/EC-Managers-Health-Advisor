package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmhealthadvisor.adapters.SinglePromoCouponsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivitySinglePromoCouponsBinding

class SinglePromoCouponsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySinglePromoCouponsBinding
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySinglePromoCouponsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        name = intent.getStringExtra("name").toString()
        binding.nameTxt.text = name
        handleBack()
        handleSetupCouponsAdapter()
    }

    private fun handleSetupCouponsAdapter() {
        var promoCouponsAdapter = SinglePromoCouponsAdapter(this)
        var linearLayoutManager = GridLayoutManager(this, 2)
        binding.couponsRv.adapter = promoCouponsAdapter
        binding.couponsRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}