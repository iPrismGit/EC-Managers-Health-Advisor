package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.iprism.ecmhealthadvisor.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding
    private var tag: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        handleTerms()
        handlePrivacy()
        handleAboutUs()
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAboutUs() {
        binding.aboutUsTxt.setOnClickListener(View.OnClickListener {
            tag = "about_us"
            name = "About Us"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handlePrivacy() {
        binding.privacyTxt.setOnClickListener(View.OnClickListener {
            tag = "privacy"
            name = "Privacy Policy"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

    private fun handleTerms() {
        binding.termsTxt.setOnClickListener(View.OnClickListener {
            tag = "terms"
            name = "Terms & Conditions"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        })
    }

}