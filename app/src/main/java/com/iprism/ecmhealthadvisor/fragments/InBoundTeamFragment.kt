package com.iprism.ecmhealthadvisor.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.adapters.MarketingTeamsAdapter

import com.iprism.ecmhealthadvisor.databinding.FragmentInBoundTeamBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.PanelAdvisor
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.HospitalViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory


class InBoundTeamFragment : Fragment() {

    private lateinit var binding: FragmentInBoundTeamBinding
    private lateinit var marketingTeamsAdapter: MarketingTeamsAdapter
    private var employeesList = mutableListOf<PanelAdvisor>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var mobileNumber: String = ""
    private val CALL_PHONE_PERMISSION_CODE = 1
    private lateinit var hospitalViewModel: HospitalViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInBoundTeamBinding.inflate(inflater, container, false)
        user = User(requireContext())
        userDetails = user.getUserDetails()
        binding.refreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.marketingTeamRv.canScrollVertically(-1)
        }
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        setupRecyclerView()
        initViewModel()
        observeTeamConnectResponse()
        loadLeads()
        handleRefreshLo()
        return binding.root
    }

    private fun initViewModel() {
        val repository = HospitalRepository()
        val factory = ViewModelFactory { HospitalViewModel(repository) }
        hospitalViewModel = ViewModelProvider(this, factory)[HospitalViewModel::class.java]
    }

    private fun setupRecyclerView() {
        marketingTeamsAdapter = MarketingTeamsAdapter(requireContext(), employeesList)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.marketingTeamRv.apply {
            layoutManager = linearLayoutManager
            adapter = marketingTeamsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled = !binding.marketingTeamRv.canScrollVertically(-1)
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
        marketingTeamsAdapter.setupListener(object : OnSingleItemClickListener {
            override fun onCallNowClick(doctorId: String, mobile: String) {
                this@InBoundTeamFragment.mobileNumber = mobile
                if (mobileNumber.isNotEmpty()){
                    makePhoneCall(this@InBoundTeamFragment.mobileNumber)
                }
            }

            override fun onSmsClick(doctorId: String, mobile: String) {
                this@InBoundTeamFragment.mobileNumber = mobile
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$mobileNumber"))
                startActivity(intent)
            }

            override fun onWhatsappClick(doctorId: String, mobile: String) {
                this@InBoundTeamFragment.mobileNumber = mobile
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
                        Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })

    }

    private fun loadLeads() {
        val request = TeamConnectApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            "inbound_marketers",
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchTeamConnectEmployees(request)
    }

    private fun refreshLeads() {
        currentPage = 1
        isLastPage = false
        employeesList.clear()
        marketingTeamsAdapter.notifyDataSetChanged()
        val request = TeamConnectApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            currentPage,
            "inbound_marketers",
            userDetails[User.ID].toString()
        )
        hospitalViewModel.fetchTeamConnectEmployees(request)
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

    private fun observeTeamConnectResponse() {
        hospitalViewModel.teamConnectResponse.observe(viewLifecycleOwner) { result ->
            if (!isAdded) return@observe

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newBookings = result.data.panel_advisors
                    if (newBookings.isNotEmpty()) {
                        employeesList.addAll(newBookings)
                        marketingTeamsAdapter.notifyDataSetChanged()
                        isLastPage = newBookings.size < limit
                        binding.marketingTeamRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.marketingTeamRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                            ToastUtils.showErrorCustomToast(requireContext(), "No Data Found!")
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    binding.marketingTeamRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                }
            }
        }
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_PERMISSION_CODE
                )
            } else {

                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is permanently denied. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
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
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is required to make phone calls. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
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