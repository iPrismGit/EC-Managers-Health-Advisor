package com.iprism.ecmhealthadvisor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.adapters.InboundTeamsAdapter

import com.iprism.ecmhealthadvisor.databinding.FragmentInBoundTeamBinding
import com.iprism.ecmhealthadvisor.utils.ToastUtils


class InBoundTeamFragment : Fragment() {

    private lateinit var binding: FragmentInBoundTeamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInBoundTeamBinding.inflate(inflater, container, false)
        setupInboundTeamAdapter()
        return binding.root
    }

    private fun setupInboundTeamAdapter() {
        var inboundTeamsAdapter = InboundTeamsAdapter(requireContext())
        var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.inboundTeamRv.adapter = inboundTeamsAdapter
        binding.inboundTeamRv.layoutManager = linearLayoutManager
        inboundTeamsAdapter.setupListener(object : OnSingleItemClickListener {
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