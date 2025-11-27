package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityAddUsersBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding

class AddUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        handleMyWhatsaapLo()
        handleMyGroupsLo()
        handleMobileContactLeadsLo()
        handleSocialLo()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleSocialLo() {
        binding.soccialGroupsLo.setOnClickListener { view ->
            var name = getString(R.string.social_groups)
            var intent = Intent(this, AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "social_groups")
            intent.putExtra("tag", "Social Group Member ")
            startActivity(intent)
        }
    }

    private fun handleMyWhatsaapLo() {
        binding.whatsappGroupLo.setOnClickListener { view ->
            var name = getString(R.string.whats_app_group_members)
            var intent = Intent(this, AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "whatsapp_groups")
            intent.putExtra("tag", "Whatsapp Group Member ")
            startActivity(intent)
        }
    }

    private fun handleMyGroupsLo() {
        binding.myGroupLo.setOnClickListener { view ->
            var name = getString(R.string.my_group_members)
            var intent = Intent(this, AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "group_members")
            intent.putExtra("tag", "My Group Member ")
            startActivity(intent)
        }
    }

    private fun handleMobileContactLeadsLo() {
        binding.mobileContactLeadsLo.setOnClickListener { view ->
            var name = getString(R.string.mobile_contact_leads)
            var intent = Intent(this, AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "leads")
            intent.putExtra("tag", "Mobile Contact Member ")
            startActivity(intent)
        }
    }

}