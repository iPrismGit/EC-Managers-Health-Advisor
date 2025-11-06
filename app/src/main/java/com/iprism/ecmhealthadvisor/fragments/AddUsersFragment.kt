package com.iprism.ecmhealthadvisor.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.activities.AddMemberActivity
import com.iprism.ecmhealthadvisor.databinding.FragmentAddUsersBinding

class AddUsersFragment : Fragment() {

    private lateinit var binding: FragmentAddUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUsersBinding.inflate(inflater, container, false)
        handleMobileContactLeadsLo()
        handleMyGroupsLo()
        handleMyWhatsaapLo()
        handleSocialLo()
        return binding.root
    }

    private fun handleSocialLo() {
        binding.soccialGroupsLo.setOnClickListener { view ->
            var name = getString(R.string.social_groups)
            var intent = Intent(requireContext(), AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "social_group")
            startActivity(intent)
        }
    }

    private fun handleMyWhatsaapLo() {
        binding.whatsappGroupLo.setOnClickListener { view ->
            var name = getString(R.string.whats_app_group_members)
            var intent = Intent(requireContext(), AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "whatsapp_group")
            startActivity(intent)
        }
    }

    private fun handleMyGroupsLo() {
        binding.myGroupLo.setOnClickListener { view ->
            var name = getString(R.string.my_group_members)
            var intent = Intent(requireContext(), AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "my_group")
            startActivity(intent)
        }
    }

    private fun handleMobileContactLeadsLo() {
        binding.mobileContactLeadsLo.setOnClickListener { view ->
            var name = getString(R.string.mobile_contact_leads)
            var intent = Intent(requireContext(), AddMemberActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("type", "mobile_contact")
            startActivity(intent)
        }
    }

}