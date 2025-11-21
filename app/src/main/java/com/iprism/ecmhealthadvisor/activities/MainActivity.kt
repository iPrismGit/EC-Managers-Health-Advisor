package com.iprism.ecmhealthadvisor.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.ViewPagerAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.LogOutDialogBinding
import com.iprism.ecmhealthadvisor.databinding.MenuBottomSheetBinding
import com.iprism.ecmhealthadvisor.utils.NetworkUtil
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.showToast
import java.util.Locale
import kotlin.text.equals

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressedOnce = false
    private lateinit var user : User
    private lateinit var userDetails : HashMap<String, String?>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        user = User(this)
        userDetails = user.getUserDetails()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val city = getSharedPreferences("user_location", MODE_PRIVATE)
            .getString("full_address", "Location Not Given!")
        binding.addressTxt.text = city
        binding.addressTxt.isSelected = true
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBottomNav()
        handleNotificationsIv()
        handleMenuImg()
        handleAddressTxt()
        askNotificationPermission()
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun handleAddressTxt() {
        binding.addressTxt.setOnClickListener { view ->
            if (binding.addressTxt.text.equals("Location Not Given!")){
                checkLocationPermissionAndFetch()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCityNameFromLocation(location: Location) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]


                val fullAddress = address.getAddressLine(0) ?: "Unknown Address"

                val editor = getSharedPreferences("user_location", MODE_PRIVATE).edit()
                editor.putString("full_address", fullAddress)
                editor.apply()

                binding.addressTxt.text = fullAddress
            } else {
                showToast("Address not found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Unable to fetch address")
        }
    }


    private fun showNoPermissionDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Permission Needed")
            .setMessage("Location permission is required to get your city name.")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun checkLocationPermissionAndFetch() {
        when {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                fetchLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchLocation()
            } else {
                showNoPermissionDialog()
            }
        }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getCityNameFromLocation(location)
            } else {

                showToast("Unable to get location. Try again.")
            }
        }
    }

    private fun handleMenuImg() {
        binding.menuIv.setOnClickListener { view ->
            showMenuBottomSheet()
        }
    }

    private fun handleNotificationsIv() {
        binding.notificationIv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        })
    }

    private fun handleBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> binding.viewPager.setCurrentItem(0, false)
                R.id.add_users_nav -> binding.viewPager.setCurrentItem(1, false)
                R.id.profile_nav -> binding.viewPager.setCurrentItem(2, false)
            }
            true
        }
    }

    private fun showMenuBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.FullScreenBottomSheetDialog)

        val bottomSheetBinding = MenuBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.crossIv.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.hospitalNameTxt.text = userDetails[User.HOSPITAL_NAME].toString()
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundColor(Color.TRANSPARENT)

        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setFlags(
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            )
            decorView.setPadding(0, getStatusBarHeight(), 0, 0)
        }

        bottomSheetBinding.contactUsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        })

        bottomSheetBinding!!.aboutUsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        })

        bottomSheetBinding!!.logOutLo.setOnClickListener(View.OnClickListener {
            showLogOutDialog()
        })

        bottomSheetDialog.show()
    }

    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

    fun showLogOutDialog() {
        val dialog = Dialog(this)
        val logoutBinding = LogOutDialogBinding.inflate(layoutInflater)
        dialog.setContentView(logoutBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.edit_text_bg)
        logoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
            user?.logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
            dialog.dismiss()

        })

        logoutBinding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        val currentItem = binding.viewPager.currentItem

        if (currentItem != 0) {
            changeFragment(0)
        } else {
            if (backPressedOnce) {
                finishAffinity()
                return
            }

            backPressedOnce = true

            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Are you sure you want to exit?",
                Snackbar.LENGTH_LONG
            )
                .setAction("Yes") {
                    finishAffinity()
                }

            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.green))
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.show()

            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 2000)
        }
    }

    private fun changeFragment(position: Int) {
        binding.viewPager.setCurrentItem(position, false)
        binding.bottomNav.menu.getItem(position).isChecked = true
    }

}