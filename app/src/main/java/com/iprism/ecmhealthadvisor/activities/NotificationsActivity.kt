package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.NotificationsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityNotificationsBinding


class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupNotificationsAdapter()
    }

    private fun setupNotificationsAdapter() {
        var notificationsAdapter = NotificationsAdapter(this)
        var linearLayOutManager = LinearLayoutManager(this)
        binding.notificationsRv.adapter = notificationsAdapter
        binding.notificationsRv.layoutManager = linearLayOutManager
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}