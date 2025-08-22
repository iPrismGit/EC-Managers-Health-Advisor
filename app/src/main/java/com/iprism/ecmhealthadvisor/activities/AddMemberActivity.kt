package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityAddMemberBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import kotlin.toString

class AddMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMemberBinding
    private var name = ""
    private var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = intent.getStringExtra("name").toString()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.titleTxt.text = name
        handleGenderLo()
        handleDownArrowIv()
        handleGenderTxt()
        handleUpArrowIv()
        handleBack()
        handleMaleTxt()
        handleFemaleTxt()
        handleOthersTxt()
        handleDownloadAppBtn()
    }

    private fun handleDownloadAppBtn() {
        binding.downloadAppBtn.setOnClickListener { view ->
            ToastUtils.showSuccessCustomToast(this, "Link Shared Successfully!")
            finish()
        }
    }

    private fun handleGenderTxt() {
        binding.genderTxt.setOnClickListener { view ->
            binding.genderTypesLo.visibility = View.GONE
            binding.upArrowImg.visibility = View.GONE
            binding.downArrowImg.visibility = View.VISIBLE
        }
    }

    private fun handleMaleTxt() {
        binding.maleTxt.setOnClickListener { view ->
            gender = binding.maleTxt.text.toString().trim()
            binding.genderTxt.text = gender
            binding.upArrowImg.visibility = View.GONE
            binding.genderTypesLo.visibility = View.GONE
            binding.downArrowImg.visibility = View.VISIBLE
        }
    }

    private fun handleFemaleTxt() {
        binding.femaleTxt.setOnClickListener { view ->
            gender = binding.femaleTxt.text.toString().trim()
            binding.genderTxt.text = gender
            binding.upArrowImg.visibility = View.GONE
            binding.genderTypesLo.visibility = View.GONE
            binding.downArrowImg.visibility = View.VISIBLE
        }
    }

    private fun handleOthersTxt() {
        binding.othersTxt.setOnClickListener { view ->
            gender = binding.othersTxt.text.toString().trim()
            binding.genderTxt.text = gender
            binding.upArrowImg.visibility = View.GONE
            binding.genderTypesLo.visibility = View.GONE
            binding.downArrowImg.visibility = View.VISIBLE
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleUpArrowIv() {
        binding.upArrowImg.setOnClickListener { view ->
            binding.genderTypesLo.visibility = View.GONE
            binding.upArrowImg.visibility = View.GONE
            binding.downArrowImg.visibility = View.VISIBLE
        }
    }

    private fun handleDownArrowIv() {
        binding.downArrowImg.setOnClickListener { view ->
            binding.genderTypesLo.visibility = View.VISIBLE
            binding.upArrowImg.visibility = View.VISIBLE
            binding.downArrowImg.visibility = View.GONE
        }
    }

    private fun handleGenderLo() {
        binding.genderLo.setOnClickListener { view ->
            binding.genderTypesLo.visibility = View.VISIBLE
            binding.upArrowImg.visibility = View.VISIBLE
            binding.downArrowImg.visibility = View.GONE
        }
    }

}