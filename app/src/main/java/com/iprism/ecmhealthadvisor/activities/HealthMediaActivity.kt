package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HealthMediasAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHealthMediaBinding

class HealthMediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHealthMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHealthMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupHealthMediaAdapter()
    }

    private fun setupHealthMediaAdapter() {
        var healthMediasAdapter = HealthMediasAdapter()
        var linearLayoutManager = LinearLayoutManager(this)
        binding.healthMediaRv.adapter = healthMediasAdapter
        binding.healthMediaRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }
}