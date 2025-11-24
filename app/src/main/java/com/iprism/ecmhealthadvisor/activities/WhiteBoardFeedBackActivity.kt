package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.WhiteBoardsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityWhiteBoardFeedBackBinding
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.WhiteBoardCategory
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.WhiteBoardFeedbackApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class WhiteBoardFeedBackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhiteBoardFeedBackBinding
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWhiteBoardFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.whiteboardsRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        handleBack()
        initViewModel()
        observeWhiteBoardFeedBackResponse()
        handleRefreshLo()
        var whiteBoardsApiRequest = WhiteBoardFeedbackApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            "",
            "",
            userDetails[User.MAIN_DATA_ID].toString(),
            "",
            "",
            userDetails[User.ID].toString(),
            "categories"
        )
        hospitalViewModel.insertWhiteBoardFeedback(whiteBoardsApiRequest)
    }

    private fun setupWhiteBoardsAdapter(categories: List<WhiteBoardCategory>) {
        var whiteBoardsAdapter = WhiteBoardsAdapter(this, categories)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.whiteboardsRv.adapter = whiteBoardsAdapter
        binding.whiteboardsRv.layoutManager = linearLayoutManager
        whiteBoardsAdapter.setupListener(object : OnWhiteBoardClickListener {
            override fun onItemClick(boardId: String, name: String) {
                var intent = Intent(this@WhiteBoardFeedBackActivity, FeedBackActivity::class.java)
                intent.putExtra("catId", boardId)
                intent.putExtra("catName", name)
                startActivity(intent)
            }
        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                var whiteBoardsApiRequest = WhiteBoardFeedbackApiRequest(
                    userDetails[User.AUTH_TOKEN].toString(),
                    "",
                    "",
                    userDetails[User.MAIN_DATA_ID].toString(),
                    "",
                    "",
                    userDetails[User.ID].toString(),
                    "categories"
                )
                hospitalViewModel.insertWhiteBoardFeedback(whiteBoardsApiRequest)
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeWhiteBoardFeedBackResponse() {
        hospitalViewModel.whiteBoardFeedbackResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.categories.isNotEmpty()) {
                        setupWhiteBoardsAdapter(result.data.categories)
                        binding.whiteboardsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        binding.whiteboardsRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.whiteboardsRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }
}