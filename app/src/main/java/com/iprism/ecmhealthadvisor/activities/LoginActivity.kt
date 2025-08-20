package com.iprism.ecmhealthadvisor.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.ecmhealthadvisor.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var tag : String = ""
    private var name : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleContinueBtn()
        handleTermsBtn()
    }

    private fun handleTermsBtn() {
        binding.termsTxt.setOnClickListener { view ->
            tag = "terms"
            name = "Terms & Conditions"
//            var intent = Intent(this, ContentPagesActivity::class.java)
//            intent.putExtra("tag", tag)
//            intent.putExtra("name", name)
//            startActivity(intent)
        }
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, OtpActivity::class.java))
        })
    }

    @SuppressLint("GestureBackNavigation", "MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
    }

}