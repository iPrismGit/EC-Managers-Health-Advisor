package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmcorporatemarketing.utils.DateTimeUtils
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityReferDiagnosticBinding
import com.iprism.ecmhealthadvisor.modals.addleads.BloodGroup
import com.iprism.ecmhealthadvisor.modals.addleads.Gender
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiRequest
import com.iprism.ecmhealthadvisor.repositoris.LeadsRepository
import com.iprism.ecmhealthadvisor.repositoris.ReferRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.LeadsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ReferViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import java.util.regex.Pattern
import kotlin.toString

class ReferDiagnosticActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReferDiagnosticBinding
    private var genderId = ""
    private var tag = ""
    private var type = ""
    private lateinit var leadsViewModel: LeadsViewModel
    private lateinit var referViewModel: ReferViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferDiagnosticBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tag = intent.getStringExtra("tag").toString()
        type = intent.getStringExtra("type").toString()
        binding.titleTxt.text = "Refer " + tag
        user = User(this)
        userDetails = user.getUserDetails()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        initViewModel()
        handleDobLo()
        handleContinueBtn()
        observeUserDropdownsResponse()
        leadsViewModel.fetchUserDropDowns()
        observeReferAdmissionOrDiagnosticResponse()
    }

    private fun initViewModel() {
        val repository = LeadsRepository()
        val factory = ViewModelFactory { LeadsViewModel(repository) }
        leadsViewModel = ViewModelProvider(this, factory)[LeadsViewModel::class.java]

        val referRepository = ReferRepository()
        val referFactory = ViewModelFactory { ReferViewModel(referRepository) }
        referViewModel = ViewModelProvider(this, referFactory)[ReferViewModel::class.java]
    }

    private fun getMobile(): String {
        return binding.mobileTxt.text.toString().trim()
    }

    private fun getName(): String {
        return binding.nameTxt.text.toString().trim()
    }

    private fun getDob(): String {
        return binding.dateOfBirthTxt.text.toString().trim()
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener { view ->
            if (getName().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Name..!")
            } else if (getMobile().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter  Patient Mobile Number..!")
            } else if (getMobile().length != 10) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            } else if (Pattern.matches("[0-5].*", getMobile())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            } else if (genderId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Patient Gender..!")
            } else if (getDob().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Patient Date of Birth..!")
            } else {
                var request = ReferApiRequest(
                    userDetails[User.AUTH_TOKEN].toString(),
                    getDob(),
                    genderId,
                    userDetails[User.MAIN_DATA_ID].toString(),
                    getMobile(),
                    getName(),
                    type,
                    userDetails[User.ID].toString()
                )
                referViewModel.referAdmissionOrDiagnostic(request)
                Log.d("ReferRequest", request.toString())
            }
        }
    }

    private fun observeReferAdmissionOrDiagnosticResponse() {
        referViewModel.referResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.continueBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Referral Request Sent ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                    binding.continueBtn.isEnabled = true
                }
            }
        }
    }

    private fun handleDobLo() {
        binding.dobLo.setOnClickListener { view ->
            DateTimeUtils.getDate(binding.dateOfBirthTxt, true)
        }
    }

    private fun setupGenderTypesAdapter(genderTypes: List<Gender>) {
        var namesList = genderTypes.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.gendersSp.adapter = adapter
        binding.gendersSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    genderId = genderTypes[position].id.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun observeUserDropdownsResponse() {
        leadsViewModel.userDropDownsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.gender.isNotEmpty()) {
                        var updatedList = result.data.gender.toMutableList()
                        updatedList.add(0, Gender("-1", "Select Gender"))
                        setupGenderTypesAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Gender Found!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

}