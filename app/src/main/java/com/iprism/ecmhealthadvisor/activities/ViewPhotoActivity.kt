package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityViewPhotoBinding
import com.iprism.ecmhealthadvisor.utils.Constants

class ViewPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPhotoBinding
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageUrl = intent.getStringExtra("imageUrl").toString()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (imageUrl.isNotEmpty()) {
            Glide.with(this).load(Constants.IMAGES_URL + imageUrl)
                .error(ContextCompat.getDrawable(this, R.drawable.img)).into(binding.facilityIv)
        } else {
            binding.facilityIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.logo))
        }
        handleBack()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

}