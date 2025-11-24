package com.iprism.ecmhealthadvisor.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.adapters.MembersAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMobileContactMembersBinding
import com.iprism.ecmhealthadvisor.modals.addleads.Lead
import com.iprism.ecmhealthadvisor.modals.addleads.LeadsApiRequest
import com.iprism.ecmhealthadvisor.repositoris.LeadsRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.LeadsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory

class MobileContactMembersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileContactMembersBinding
    private var tag = ""
    private var type = ""
    private lateinit var leadsAdapter: MembersAdapter
    private var leadsList = mutableListOf<Lead>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var mobileNumber: String = ""
    private val CALL_PHONE_PERMISSION_CODE = 1
    private lateinit var leadsViewModel: LeadsViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMobileContactMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tag = intent.getStringExtra("tag").toString()
        type = intent.getStringExtra("type").toString()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.nameTxt.text = tag
        user = User(this)
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.leadsRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.green)
        )
        setupRecyclerView()
        initViewModel()
        observeLeadsResponse()
        loadLeads()
        handleRefreshLo()
        handleBack()
    }

    private fun initViewModel() {
        val repository = LeadsRepository()
        val factory = ViewModelFactory { LeadsViewModel(repository) }
        leadsViewModel = ViewModelProvider(this, factory)[LeadsViewModel::class.java]
    }

    private fun setupRecyclerView() {
        leadsAdapter = MembersAdapter(this, leadsList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.leadsRv.apply {
            layoutManager = linearLayoutManager
            adapter = leadsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled = !binding.leadsRv.canScrollVertically(-1)
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
        leadsAdapter.setupListener(object : OnSingleItemClickListener {
            override fun onCallNowClick(doctorId: String, mobile: String) {
                this@MobileContactMembersActivity.mobileNumber = mobile
                if (mobileNumber.isNotEmpty()) {
                    makePhoneCall(this@MobileContactMembersActivity.mobileNumber)
                }
            }

            override fun onSmsClick(doctorId: String, mobile: String) {
                this@MobileContactMembersActivity.mobileNumber = mobile
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$mobileNumber"))
                startActivity(intent)
            }

            override fun onWhatsappClick(doctorId: String, mobile: String) {
                this@MobileContactMembersActivity.mobileNumber = mobile
                val url = "https://wa.me/+91 $mobileNumber"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)

                try {
                    intent.setPackage("com.whatsapp")
                    startActivity(intent)
                } catch (e1: Exception) {
                    try {

                        intent.setPackage("com.whatsapp.w4b")
                        startActivity(intent)
                    } catch (e2: Exception) {
                        Toast.makeText(
                            this@MobileContactMembersActivity,
                            "WhatsApp not installed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })

    }

    private fun loadLeads() {
        val request = LeadsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            type
        )
        leadsViewModel.fetchTotalLeads(request)
    }

    private fun refreshLeads() {
        currentPage = 1
        isLastPage = false
        leadsList.clear()
        leadsAdapter.notifyDataSetChanged()
        val request = LeadsApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            userDetails[User.ID].toString(),
            type
        )
        leadsViewModel.fetchTotalLeads(request)
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage += 1
        loadLeads()
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshLeads()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun observeLeadsResponse() {
        leadsViewModel.leadsResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.leads
                    if (newBookings.isNotEmpty()) {
                        leadsList.addAll(newBookings)
                        leadsAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.leadsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.leadsRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(this, "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.leadsRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
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

    private fun handleBack() {
        binding.backImg.setOnClickListener { view ->
            finish()
        }
    }
}