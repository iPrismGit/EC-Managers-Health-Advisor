package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.ecmhealthadvisor.R

import com.iprism.ecmhealthadvisor.adapters.TeamConnectPagerAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityTeamConnectBinding

class TeamConnectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamConnectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTeamConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = TeamConnectPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBack()
        handleMarketingTeamLo()
        handleInBoundTeamLo()
    }

    private fun handleInBoundTeamLo() {
        binding.inboundTeamLo.setOnClickListener(View.OnClickListener {
            setupButtonsStyling(binding.inboundTeamLo, binding.marketingTeamLo)
            binding.viewPager.setCurrentItem(1, false)
        })
    }

    private fun handleMarketingTeamLo() {
        binding.marketingTeamLo.setOnClickListener(View.OnClickListener {
            setupButtonsStyling(binding.marketingTeamLo, binding.inboundTeamLo)
            binding.viewPager.setCurrentItem(0, false)
        })
    }

    private fun setupButtonsStyling(textView: TextView, textView1: TextView){
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        textView1.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}