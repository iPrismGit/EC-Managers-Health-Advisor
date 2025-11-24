package com.iprism.ecmhealthadvisor.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmhealthadvisor.databinding.ActivityContactUsBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.ContactUsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import java.util.regex.Pattern
import kotlin.toString

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private var mobileNumber: String = ""
    private val CALL_PHONE_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleCallNow()
        handleBack()
        handleContinueClick()
        user = User(this)
        userDetails = user.getUserDetails()
        handleBack()
        initViewModel()
        observeContactUsCallResponse()
        observeContactUsInsertResponse()
        var contactUsApiRequest = ContactUsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            "",
            "",
            "",
            "",
            userDetails[User.ID].toString(),
            "view"
        )
        hospitalViewModel.contactUs(contactUsApiRequest)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCallNow() {
        binding.callNowBtn.setOnClickListener(View.OnClickListener {
            makePhoneCall(mobileNumber)
        })
    }

    private fun isValidPersonName(name: String): Boolean {
        val regex = Regex("^[A-Za-z][A-Za-z .'-]{1,49}$")
        return name.matches(regex)
    }

    private fun handleContinueClick() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (getName().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Name")
            } else if (!isValidPersonName(getName())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Name!")
            } else if (getEmailId().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Email ID")
            } else if (!isValidEmailAddress(getEmailId())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Email ID")
            } else if (getMobileNumber().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Mobile Number")
            } else if (getMobileNumber().length != 10) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number")
            } else if (Pattern.matches("[0-5].*", getMobileNumber())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter A Valid Mobile Number")
            } else if (getMessage().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Message")
            } else {
                var contactUsApiRequest = ContactUsApiRequest(
                    userDetails[User.AUTH_TOKEN].toString(),
                    userDetails[User.MAIN_DATA_ID].toString(),
                    getEmailId(),
                    getMessage(),
                    getMobileNumber(),
                    getName(),
                    userDetails[User.ID].toString(),
                    "insert"
                )
                hospitalViewModel.contactUsInsert(contactUsApiRequest)
            }
        })
    }

    private fun observeContactUsCallResponse() {
        hospitalViewModel.contactUsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    mobileNumber = result.data.mobile.toString()
                    ToastUtils.showSuccessCustomToast(this, "Details Fetched Successfully!")
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeContactUsInsertResponse() {
        hospitalViewModel.contactUsInsertResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.continueBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.continueBtn.isEnabled = true
                    ToastUtils.showSuccessCustomToast(this, "Report Submitted Successfully!")
                    finish()
                }

                is UiState.Error -> {
                    binding.continueBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun getMobileNumber(): String {
        return binding.phoneNumberTxt.text.toString().trim()
    }

    private fun getName(): String {
        return binding.nameTxt.text.toString().trim()
    }

    private fun getEmailId(): String {
        return binding.emailIdTxt.text.toString().trim()
    }

    private fun getMessage(): String {
        return binding.messageTxt.text.toString().trim()
    }

    private fun isValidEmailAddress(email: String): Boolean {
        val ePattern = "^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        val pattern = Pattern.compile(ePattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_PERMISSION_CODE
                )
            } else {

                AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is permanently denied. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mobileNumber)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is required to make phone calls. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

}