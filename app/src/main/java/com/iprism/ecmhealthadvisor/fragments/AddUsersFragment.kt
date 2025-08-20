package com.iprism.ecmhealthadvisor.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.ecmhealthadvisor.databinding.FragmentAddUsersBinding

class AddUsersFragment : Fragment() {

    private lateinit var binding: FragmentAddUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUsersBinding.inflate(inflater, container, false)
//        handleMyLeadsLo()
//        handleCompaniesLo()
//        handleMyHealthAdvisorLo()
//        handleOutBoundMarketingTeamLo()
//        handlePanelAdvisorsLo()
        return binding.root
    }

//    private fun handlePanelAdvisorsLo() {
//        binding.panelAdvisorsLo.setOnClickListener { view ->
//            startActivity(Intent(requireContext(), AddPanelAdvisorActivity::class.java))
//        }
//    }
//
//    private fun handleMyHealthAdvisorLo() {
//        binding.healthAdvisersLo.setOnClickListener { view ->
//            startActivity(Intent(requireContext(), AddHealthAdvisorActivity::class.java))
//        }
//    }
//
//    private fun handleOutBoundMarketingTeamLo() {
//        binding.clientRelationsLo.setOnClickListener { view ->
//            startActivity(Intent(requireContext(), AddOutBoundMarketingTeamActivity::class.java))
//        }
//    }
//
//    private fun handleCompaniesLo() {
//        binding.companiesLo.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(requireContext(), CreateCompaniesActivity::class.java))
//        })
//    }
//
//    private fun handleMyLeadsLo() {
//        binding.myLeadsLo.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(requireContext(), CreateMyLeadsActivity::class.java))
//        })
//    }

}