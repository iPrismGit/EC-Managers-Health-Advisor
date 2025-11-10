package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmhealthadvisor.databinding.ActivityMyTaskProgressBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyTaskProgressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyTaskProgressBinding
    private var currentCalendar = Calendar.getInstance()
    private var selectedCalendar = Calendar.getInstance()
    private var selectedMonth = 0
    private var selectedYear = 0
    private var type = ""
    private var title = ""
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTaskProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        type = intent.getStringExtra("type").toString()
        title = intent.getStringExtra("title").toString()
        binding.titleTxt.text = title
        user = User(this)
        userDetails = user.getUserDetails()
        initViewModel()
        handleBack()
        updateDateText()
        handleLeftArrow()
        handleRightArrow()
        observeTaskPerformanceDetailsResponse()
        var taskAndPerformanceApiRequest = TaskAndPerformanceApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            selectedMonth.toString(),
            type,
            userDetails[User.ID].toString(),
            "view",
            selectedYear.toString()
        )
        hospitalViewModel.fetchTaskPerformanceDetails(taskAndPerformanceApiRequest)
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun handleRightArrow() {
        binding.rightArrowIv.setOnClickListener { view ->
            selectedCalendar.add(Calendar.MONTH, 1)
            selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
            selectedYear = selectedCalendar.get(Calendar.YEAR)
            updateDateText()

        }
    }

    private fun handleLeftArrow() {
        binding.leftArrowIv.setOnClickListener { view ->
            selectedCalendar.add(Calendar.MONTH, -1)
            selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
            selectedYear = selectedCalendar.get(Calendar.YEAR)
            updateDateText()

        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun updateDateText() {
        val dateFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
        binding.dateTxt.text = dateFormat.format(selectedCalendar.time)

        val isCurrentMonth =
            selectedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    selectedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)

        binding.rightArrowIv.visibility = if (isCurrentMonth) View.GONE else View.VISIBLE
        selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
        selectedYear = selectedCalendar.get(Calendar.YEAR)
        var taskAndPerformanceApiRequest = TaskAndPerformanceApiRequest(userDetails[User.AUTH_TOKEN].toString(), userDetails[User.MAIN_DATA_ID].toString(), selectedMonth.toString(), type, userDetails[User.ID].toString(), "view", selectedYear.toString())
        hospitalViewModel.fetchTaskPerformanceDetails(taskAndPerformanceApiRequest)
        Log.d("taskRequest", taskAndPerformanceApiRequest.toString())
    }

    private fun observeTaskPerformanceDetailsResponse() {
        hospitalViewModel.taskPerformanceDetailsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.mainLo.visibility = View.VISIBLE
                    binding.noDataTxt.visibility = View.GONE
                    binding.leadCountTxt.text = result.data.lead_task.toString()
                    binding.achivedCountTxt.text = result.data.achieved.toString()
                    binding.pendingCountTxt.text = result.data.pending.toString()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.mainLo.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

}