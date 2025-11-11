package com.iprism.ecmhealthadvisor.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.iprism.ecmhealthadvisor.databinding.ActivitySplashBinding
import com.iprism.ecmhealthadvisor.utils.NetworkUtil
import com.iprism.ecmhealthadvisor.utils.NoInternetDialog
import com.iprism.ecmhealthadvisor.utils.User
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.Locale

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 1001
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        user = User(this)
        userDetails = user.getUserDetails()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.logoImg.scaleX = 0f
        binding.logoImg.scaleY = 0f
        binding.logoImg.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocationAndProceed()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    private fun getLocationAndProceed() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                try {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val area = addresses?.get(0)?.subLocality
                    val city = addresses?.get(0)?.locality ?: addresses?.get(0)?.subAdminArea ?: "Unknown"
                    val cityText = area ?: city

                    getSharedPreferences("user_location", MODE_PRIVATE).edit()
                        .putString("city_name", cityText)
                        .apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            checkInternetAndProceed()
        }.addOnFailureListener {
            checkInternetAndProceed()
        }
    }

    private fun checkInternetAndProceed() {
        lifecycleScope.launch {
            val isNetwork = NetworkUtil.isNetworkAvailable(this@SplashActivity)
            val hasInternet = if (isNetwork) NetworkUtil.hasInternetAccess() else false

            if (!isNetwork || !hasInternet) {
                NoInternetDialog.show(this@SplashActivity)

                Handler(Looper.getMainLooper()).postDelayed({
                    checkInternetAndProceed()
                }, 2000)
            } else {

                NoInternetDialog.dismiss()
                proceedToNextScreen()
            }
        }
    }

    private fun proceedToNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (user.isUserLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                getLocationAndProceed()
            } else {
                checkInternetAndProceed()
            }
        }
    }
}
