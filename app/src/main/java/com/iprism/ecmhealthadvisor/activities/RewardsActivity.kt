package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityRewardsBinding
import com.iprism.ecmhealthadvisor.databinding.WithdrawalRequestBottomSheetBinding
import com.iprism.ecmhealthadvisor.modals.rewards.RewardsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.RewardsRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.RewardsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory


class RewardsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardsBinding
    private lateinit var rewardsViewModel: RewardsViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private lateinit var withdrawBinding: WithdrawalRequestBottomSheetBinding
    private var redeemableAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRewardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User(this)
        userDetails = user.getUserDetails()
        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate_bounce)
        binding.starOneImg.startAnimation(anim)
        binding.starTwoImg.startAnimation(anim)
        handleBack()
        initViewModel()
        observeRewardsResponse()
        handleRequestBtn()
        observeRequestWithdrawalResponse()
        var request = RewardsApiRequest(
            0, userDetails[User.AUTH_TOKEN].toString(), userDetails[User.MAIN_DATA_ID].toString(),
            userDetails[User.ID].toString(), "view", 0
        )
        rewardsViewModel.fetchRewards(request)
    }

    private fun handleRequestBtn() {
        binding.withdrawalBtn.setOnClickListener { view ->
            openWithdrawalSheet()
        }
    }

    private fun openWithdrawalSheet() {
        val dialog = BottomSheetDialog(this)
        withdrawBinding = WithdrawalRequestBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(withdrawBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        withdrawBinding.crossImg.setOnClickListener { view ->
            dialog.dismiss()
        }

        withdrawBinding.submitBtn.setOnClickListener {

            val amount = withdrawBinding.amountTxt.text.toString().trim()

            if (amount.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Amount..!")
                return@setOnClickListener
            }

            if (amount.matches(Regex("0+"))) {
                ToastUtils.showErrorCustomToast(this, "Amount Should not be Zero..!")
                return@setOnClickListener
            }

            val enteredAmount = amount.toIntOrNull()
            if (enteredAmount == null) {
                ToastUtils.showErrorCustomToast(this, "Invalid amount")
                return@setOnClickListener
            }

            if (enteredAmount > redeemableAmount) {
                ToastUtils.showErrorCustomToast(this, "Insufficient Amount..!")
                return@setOnClickListener
            }

            val request = RewardsApiRequest(
                enteredAmount,
                userDetails[User.AUTH_TOKEN].toString(),
                userDetails[User.MAIN_DATA_ID].toString(),
                userDetails[User.ID].toString(),
                "request",
                redeemableAmount
            )

            rewardsViewModel.requestWithdraw(request)
        }

    }


    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = RewardsRepository()
        val factory = ViewModelFactory { RewardsViewModel(repository) }
        rewardsViewModel = ViewModelProvider(this, factory)[RewardsViewModel::class.java]
    }

    private fun observeRewardsResponse() {
        rewardsViewModel.rewardsResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.mainLo.visibility = View.VISIBLE
                    binding.totalAmountTxt.text = "₹" + result.data.total_commission
                    binding.withdrawalAmountTxt.text = "₹" + result.data.total_credited
                    binding.redemableAmountTxt.text = "₹" + result.data.wallet_balance
                    binding.todayCountTxt.text =
                        "No.of Persons Added : " + result.data.today.toString()
                    binding.yesterdayCountTxt.text =
                        "No.of Persons Added : " + result.data.yesterday.toString()
                    binding.thisWeekCountTxt.text =
                        "No.of Persons Added : " + result.data.this_week.toString()
                    binding.requestedAmountTxt.text =
                        "Your Withdrawal Request amount ₹" + result.data.total_requested.toString() + " is in Progress.."
                    redeemableAmount = result.data.wallet_balance
                    if (result.data.total_requested == 0) {
                        binding.requestedAmountTxt.visibility = View.GONE
                    } else {
                        binding.requestedAmountTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.mainLo.visibility = View.GONE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun observeRequestWithdrawalResponse() {
        rewardsViewModel.requestResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    withdrawBinding.progress.showProgress()
                    withdrawBinding.submitBtn.isEnabled = false
                }

                is UiState.Success -> {
                    withdrawBinding.progress.hideProgress()
                    withdrawBinding.submitBtn.isEnabled = true
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Withdrawal Request Sent ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    withdrawBinding.progress.hideProgress()
                    withdrawBinding.submitBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

}