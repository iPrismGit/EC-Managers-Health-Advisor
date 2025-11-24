package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmhealthadvisor.databinding.ActivityFeedBackBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.WhiteBoardFeedbackApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import kotlin.toString

class FeedBackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBackBinding
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private var catId = ""
    private var catName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        catId = intent.getStringExtra("catId").toString()
        catName = intent.getStringExtra("catName").toString()
        binding.titleTxt.text = catName
        user = User(this)
        userDetails = user.getUserDetails()
        handleBack()
        initViewModel()
        handleSubmitBtn()
        observeWhiteBoardFeedBackResponse()
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            if (getName().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Name..!")
            } else if (getRoomNumber().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Room Number..!")
            } else if (getRoomNumber().matches(Regex("0+"))){
                ToastUtils.showErrorCustomToast(this, "Room NumberShould not be Zero..!")
            } else if (getFeedBack().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Feedback..!")
            } else{
                var whiteBoardFeedbackApiRequest = WhiteBoardFeedbackApiRequest(
                    userDetails[User.AUTH_TOKEN].toString(),
                    catId,
                    getFeedBack(),
                    userDetails[User.MAIN_DATA_ID].toString(),
                    getName(),
                    getRoomNumber(),
                    userDetails[User.ID].toString(),
                    "insert"
                )
                hospitalViewModel.insertWhiteBoardFeedback(whiteBoardFeedbackApiRequest)
            }
        })
    }

    private fun getName() : String{
        return binding.nameTxt.text.toString().trim()
    }

    private fun getRoomNumber() : String{
        return binding.roomNumberTxt.text.toString().trim()
    }

    private fun getFeedBack() : String{
        return binding.feedbackTxt.text.toString().trim()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun observeWhiteBoardFeedBackResponse() {
        hospitalViewModel.whiteBoardFeedbackResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.submitBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Feedback Sent")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.submitBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

}