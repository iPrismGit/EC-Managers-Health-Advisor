package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.interfaces.OnDoctorItemClickListener
import com.iprism.ecmhealthadvisor.adapters.MyTasksAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMyTasksBinding

class MyTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        handleSocialLo()
        handleMyGroupsLo()
        handleMyWhatsaapLo()
        handleMobileContactLeadsLo()
    }

    private fun handleSocialLo() {
        binding.soccialGroupsLo.setOnClickListener { view ->
            var name = getString(R.string.social_groups)
            var intent = Intent(this, MyTaskProgressActivity::class.java)
            intent.putExtra("title", name)
            intent.putExtra("type", "social_group")
            startActivity(intent)
        }
    }

    private fun handleMyWhatsaapLo() {
        binding.whatsappGroupLo.setOnClickListener { view ->
            var name = getString(R.string.whats_app_group_members)
            var intent = Intent(this, MyTaskProgressActivity::class.java)
            intent.putExtra("title", name)
            intent.putExtra("type", "whatsapp_group")
            startActivity(intent)
        }
    }

    private fun handleMyGroupsLo() {
        binding.myGroupLo.setOnClickListener { view ->
            var name = getString(R.string.my_group_members)
            var intent = Intent(this, MyTaskProgressActivity::class.java)
            intent.putExtra("title", name)
            intent.putExtra("type", "my_group")
            startActivity(intent)
        }
    }

    private fun handleMobileContactLeadsLo() {
        binding.mobileContactLeadsLo.setOnClickListener { view ->
            var name = getString(R.string.mobile_contact_leads)
            var intent = Intent(this, MyTaskProgressActivity::class.java)
            intent.putExtra("title", name)
            intent.putExtra("type", "mobile_contact")
            startActivity(intent)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}