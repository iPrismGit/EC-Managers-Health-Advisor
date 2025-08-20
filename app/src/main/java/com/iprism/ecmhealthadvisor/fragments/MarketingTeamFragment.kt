package com.iprism.ecmhealthadvisor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.MarketingTeamsAdapter
import com.iprism.ecmcorporatemarketing.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.databinding.FragmentMarketingTeamBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils


class MarketingTeamFragment : Fragment() {

    private lateinit var binding: FragmentMarketingTeamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarketingTeamBinding.inflate(inflater, container, false)
        setupMarketingTeamsAdapter()
        return binding.root
    }

    private fun setupMarketingTeamsAdapter() {
        var marketingTeamsAdapter = MarketingTeamsAdapter(requireContext())
        var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.marketingTeamRv.adapter = marketingTeamsAdapter
        binding.marketingTeamRv.layoutManager = linearLayoutManager
        marketingTeamsAdapter.setupListener(object : OnSingleItemClickListener {
            override fun onCallNowClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(requireContext(), "Calling to the Marketing Team!")
            }

            override fun onSmsClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(requireContext(), "Messaging to the Marketing Team!")
            }

            override fun onWhatsappClick(doctorId: String, mobile: String) {
                ToastUtils.showSuccessCustomToast(requireContext(), "Navigate to WhatsApp!")
            }

        })
    }

}