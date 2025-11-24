package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.net.Uri
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
import com.iprism.ecmhealthadvisor.adapters.TestimonialAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityTestimonialVideosBinding
import com.iprism.ecmhealthadvisor.interfaces.TestimonialClickListener
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Video
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class TestimonialVideosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestimonialVideosBinding
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private var videosList = mutableListOf<Video>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var type = ""
    private var title = ""
    private lateinit var testimonialsAdapter: TestimonialAdapter
    private lateinit var hospitalViewModel: HospitalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTestimonialVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = intent.getStringExtra("title").toString()
        type = intent.getStringExtra("type").toString()
        binding.titleTxt.text = title
        initViewModel()
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.testimonialsRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        handleBack()
        setupRecyclerView()
        observeTestimonialsResponse()
        loadContent()
        handleRefreshLo()

    }

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun setupRecyclerView() {
        testimonialsAdapter = TestimonialAdapter(this, videosList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.testimonialsRv.apply {
            layoutManager = linearLayoutManager
            adapter = testimonialsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.testimonialsRv.canScrollVertically(-1)
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
            testimonialsAdapter.setupListener(object : TestimonialClickListener {
                override fun onVideoClick(url: String) {
                    openYouTubeLink(url)
                }

            })
        }

    }

    private fun loadContent() {
        val request = TestimonialVideosApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            type
        )
        hospitalViewModel.fetchTestimonialVideos(request)
        Log.d("ApiRequest", request.toString())
    }

    private fun refreshData() {
        currentPage = 1
        isLastPage = false
        videosList.clear()
        testimonialsAdapter.notifyDataSetChanged()
        val request = TestimonialVideosApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            type
        )
        hospitalViewModel.fetchTestimonialVideos(request)
        Log.d("ApiRequest", request.toString())
    }

    private fun openYouTubeLink(url: String) {
        try {

            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intentApp.setPackage("com.google.android.youtube")
            startActivity(intentApp)
        } catch (e: Exception) {

            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intentBrowser)
        }
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        loadContent()
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshData()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeTestimonialsResponse() {
        hospitalViewModel.testimonialVideosResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.videos
                    if (newBookings.isNotEmpty()) {
                        videosList.addAll(newBookings)
                        testimonialsAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.testimonialsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.testimonialsRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.testimonialsRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }
}