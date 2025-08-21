package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.HospitalTieUpsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityDigitalBrandingsBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding

class DigitalBrandingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDigitalBrandingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDigitalBrandingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupDigitalPromosAdapter()
        handleBrandingViewAllBtn()
        handleFestivalsViewAllBtn()
        handleOccasionViewAllBtn()
        handleBirthdaysViewAllBtn()
        handleAddBranding()
        handleAddFestivals()
        handleAddOccasuons()
        handleAddBirthdays()
    }

    private fun handleAddBranding() {
        binding.addBrandingPromoLo.setOnClickListener { view ->
            var intent = Intent(this, AddDeigitalBrandingActivity::class.java)
            intent.putExtra("tag", "Branding")
            startActivity(intent)
        }
    }

    private fun handleAddFestivals() {
        binding.addFestivalPromoLo.setOnClickListener { view ->
            var intent = Intent(this, AddDeigitalBrandingActivity::class.java)
            intent.putExtra("tag", "Festivals")
            startActivity(intent)
        }
    }

    private fun handleAddOccasuons() {
        binding.addOccasionPromoLo.setOnClickListener { view ->
            var intent = Intent(this, AddDeigitalBrandingActivity::class.java)
            intent.putExtra("tag", "Occasions")
            startActivity(intent)
        }
    }

    private fun handleAddBirthdays() {
        binding.addBirthdayPromoLo.setOnClickListener { view ->
            var intent = Intent(this, AddDeigitalBrandingActivity::class.java)
            intent.putExtra("tag", "Birthdays")
            startActivity(intent)
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun setupDigitalPromosAdapter() {
        var hospitalTieUpsAdapter = HospitalTieUpsAdapter(this)
        binding.festivalsRv.adapter = hospitalTieUpsAdapter
        binding.brandingRv.adapter = hospitalTieUpsAdapter
        binding.occasionPromosRv.adapter = hospitalTieUpsAdapter
        binding.birthdaysRv.adapter = hospitalTieUpsAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager3 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.festivalsRv.layoutManager = linearLayoutManager
        binding.brandingRv.layoutManager = linearLayoutManager1
        binding.occasionPromosRv.layoutManager = linearLayoutManager2
        binding.birthdaysRv.layoutManager = linearLayoutManager3
    }

    private fun handleBrandingViewAllBtn() {
        binding.brandingViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ViewAllDigitalBrandingsActivity::class.java)
            intent.putExtra("name", "Branding")
            startActivity(intent)
        })
    }

    private fun handleFestivalsViewAllBtn() {
        binding.festivalsViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ViewAllDigitalBrandingsActivity::class.java)
            intent.putExtra("name", "Festivals")
            startActivity(intent)
        })
    }

    private fun handleOccasionViewAllBtn() {
        binding.occasionViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ViewAllDigitalBrandingsActivity::class.java)
            intent.putExtra("name", "Occasions")
            startActivity(intent)
        })
    }

    private fun handleBirthdaysViewAllBtn() {
        binding.birthdaysViewAllBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ViewAllDigitalBrandingsActivity::class.java)
            intent.putExtra("name", "Birthdays")
            startActivity(intent)
        })
    }

}