package com.iprism.ecmhealthadvisor.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.adapters.NotificationsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityNotificationsBinding
import com.iprism.ecmhealthadvisor.modals.notification.Notification
import com.iprism.ecmhealthadvisor.modals.notification.NotificationsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.NotificationsRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.NotificationsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var notificationsList = mutableListOf<Notification>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        initViewModel()
        setupRecyclerView()
        observeBookingsResponse()
        loadNotifications()
        handleRefreshLo()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupRecyclerView() {
        notificationsAdapter = NotificationsAdapter(this, notificationsList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.notificationsRv.apply {
            layoutManager = linearLayoutManager
            adapter = notificationsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled = !binding.notificationsRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreItems()
                        }
                    }
                }
            })
        }

    }

    private fun initViewModel() {
        val repository = NotificationsRepository()
        val factory = ViewModelFactory { NotificationsViewModel(repository) }
        notificationsViewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]
    }

    private fun loadNotifications() {
        val request = NotificationsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            "view",
            userDetails[User.MAIN_DATA_ID].toString()
        )
        Log.d("NotificationsRequest", request.toString())
        notificationsViewModel.fetchNotifications(request)
    }

    private fun refreshBookings() {
        currentPage = 1
        isLastPage = false
        notificationsList.clear()
        notificationsAdapter.notifyDataSetChanged()
        val request = NotificationsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            1,
            userDetails[User.ID].toString(),
            "view",
            userDetails[User.MAIN_DATA_ID].toString()
        )
        Log.d("NotificationsRequest", request.toString())
        notificationsViewModel.fetchNotifications(request)
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        loadNotifications()
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshBookings()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeBookingsResponse() {
        notificationsViewModel.notificationsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.notifications
                    if (newBookings.isNotEmpty()) {
                        notificationsList.addAll(newBookings)
                        notificationsAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.notificationsRv.visibility = View.VISIBLE
                        binding.noBookingsLo.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.notificationsRv.visibility = View.GONE
                            binding.noBookingsLo.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (notificationsList.isEmpty()) {
                        binding.notificationsRv.visibility = View.GONE
                        binding.noBookingsLo.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.notificationsRv.visibility = View.VISIBLE
                        binding.noBookingsLo.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

}