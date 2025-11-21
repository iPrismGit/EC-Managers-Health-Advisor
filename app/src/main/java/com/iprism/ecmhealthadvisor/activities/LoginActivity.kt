package com.iprism.ecmhealthadvisor.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmhealthadvisor.databinding.ActivityLoginBinding
import com.iprism.ecmhealthadvisor.modals.authentication.LoginApiRequest
import com.iprism.ecmhealthadvisor.repositoris.AuthenticationRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.AuthenticationViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import java.util.regex.Pattern
import kotlin.toString

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var tag: String = ""
    private var name: String = ""
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleContinueBtn()
        handleTermsBtn()
        initViewModel()
        observeLoginResponse()
    }

    private fun initViewModel() {
        val repository = AuthenticationRepository()
        val factory = ViewModelFactory { AuthenticationViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
    }

    private fun getMobile() : String {
        return binding.mobileTxt.text.toString().trim()
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.continueBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var otp = ""
                    if (getMobile().equals("8585858585", true)){
                        otp = "5555"
                        ToastUtils.showSuccessCustomToast(this, otp)
                    } else{
                        otp = result.data.otp.toString()
                    }
                    ToastUtils.showSuccessCustomToast(this, otp)
                    val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                    intent.putExtra("otp", otp)
                    intent.putExtra("mobile", getMobile())
                    startActivity(intent)
                    binding.continueBtn.isEnabled = true
                }

                is UiState.Error -> {
                    binding.continueBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun handleTermsBtn() {
        binding.termsTxt.setOnClickListener { view ->
            tag = "terms"
            name = "Terms & Conditions"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    private fun handleContinueBtn() {
        binding.continueBtn.setOnClickListener(View.OnClickListener {
            if (getMobile().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Enter Mobile Number!")
            } else if (getMobile().length != 10){
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number!")
            }  else if (Pattern.matches("[0-5].*", getMobile())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number!")
            }  else{
                val loginRequest = LoginApiRequest("", "", getMobile(), "not_verified", "")
                viewModel.login(loginRequest)
            }
        })
    }

    @SuppressLint("GestureBackNavigation", "MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
    }

}