package com.iprism.ecmhealthadvisor.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.ViewPagerAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.LogOutDialogBinding
import com.iprism.ecmhealthadvisor.databinding.MenuBottomSheetBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBottomNav()
        handleNotificationsIv()
        handleMenuImg()
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
        val bottomSheetBinding = MenuBottomSheetBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(bottomSheetBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val params = it.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams = params
        }

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.isFitToContents = false
        bottomSheetDialog.behavior.skipCollapsed = true
        bottomSheetDialog.behavior.peekHeight = 0

        bottomSheetDialog.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val parentLayout =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                view.setBackgroundResource(R.drawable.bottom_sheet_background)

                val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(20, 80, 20, 0)
                view.layoutParams = layoutParams
            }
        }

        bottomSheetBinding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        bottomSheetBinding.contactUsLo.setOnClickListener(View.OnClickListener {
          //  startActivity(Intent(this, ContactUsActivity::class.java))
        })

        bottomSheetBinding!!.aboutUsLo.setOnClickListener(View.OnClickListener {
         //   startActivity(Intent(this, AboutUsActivity::class.java))
        })

        bottomSheetBinding!!.logOutLo.setOnClickListener(View.OnClickListener {
            showLogOutDialog()
        })

        bottomSheetDialog.show()
    }

    fun showLogOutDialog() {
        val dialog = Dialog(this)
        val logoutBinding = LogOutDialogBinding.inflate(layoutInflater)
        dialog.setContentView(logoutBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.edit_text_bg)
        logoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
//            user?.logoutUser()
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