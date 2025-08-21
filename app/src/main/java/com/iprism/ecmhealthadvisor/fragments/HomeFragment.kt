package com.iprism.ecmhealthadvisor.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.activities.AddUsersActivity
import com.iprism.ecmhealthadvisor.activities.HealthMediaActivity
import com.iprism.ecmhealthadvisor.activities.HospitalDoctorsActivity
import com.iprism.ecmhealthadvisor.activities.HospitalFecilitiesActivity
import com.iprism.ecmhealthadvisor.activities.HospitalHodsActivity
import com.iprism.ecmhealthadvisor.activities.HospitalTariffsListActivity
import com.iprism.ecmhealthadvisor.activities.HospitalTieupsActivity
import com.iprism.ecmhealthadvisor.activities.MobileContactMembersActivity
import com.iprism.ecmhealthadvisor.activities.MyTasksActivity
import com.iprism.ecmhealthadvisor.activities.PromoCouponsActivity
import com.iprism.ecmhealthadvisor.activities.ReferDiagnosticActivity
import com.iprism.ecmhealthadvisor.activities.TeamConnectActivity
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        handleAddUSersLo()
        handleHospitalDoctorsLo()
        handleHospitalHodsLo()
        handleTeamConnectLo()
        handlePromoCouponsLo()
        handleHospitalFecilitiesLo()
        handleHospitalTieupsLo()
        handleHospitalTarrifsLo()
        handleHospitalUpdatesLo()
        handlePatientTestimonialsLo()
        handleHealthTalks()
        handleHealthMedia()
        handleMobileContactMembers()
        handleMyGroupMembersLo()
        handleSocialGroupMembers()
        handleWhatsappGroupMembersLo()
        handleTargetVsPerformanceLo()
        handleReferDiagnosticLo()
        return binding.root
    }

    private fun handleReferDiagnosticLo() {
        binding.referDiagnosticLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), ReferDiagnosticActivity::class.java))
        }
    }

    private fun handleTargetVsPerformanceLo() {
        binding.targetPerformanceLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), MyTasksActivity::class.java))
        }
    }

    private fun handleWhatsappGroupMembersLo() {
        binding.whatsappGroupLo.setOnClickListener { view ->
            var value = getString(R.string.whats_app_group_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            startActivity(intent)
        }
    }

    private fun handleSocialGroupMembers() {
        binding.socialGroupMembersLo.setOnClickListener { view ->
            var value = getString(R.string.social_group_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            startActivity(intent)
        }
    }

    private fun handleMyGroupMembersLo() {
        binding.myGroupMembersLo.setOnClickListener { view ->
            var value = getString(R.string.my_group_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            startActivity(intent)
        }
    }

    private fun handleMobileContactMembers() {
        binding.mobileContactMembersLo.setOnClickListener { view ->
            var value = getString(R.string.mobile_contact_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            startActivity(intent)
        }
    }


    private fun handleHealthMedia() {
        binding.healthMediaLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), HealthMediaActivity::class.java))
        }
    }

    private fun handleHealthTalks() {
        binding.healthTalksLo.setOnClickListener { view ->
            ToastUtils.showErrorCustomToast(requireContext(), "There are no Screens in Figma!")
        }
    }


    private fun handleHospitalUpdatesLo() {
        binding.hospitalUpdatesLo.setOnClickListener(View.OnClickListener {
            ToastUtils.showErrorCustomToast(requireContext(), "There are no Screens in Figma!")
        })
    }

    private fun handlePatientTestimonialsLo() {
        binding.patientTestimonialsLo.setOnClickListener(View.OnClickListener {
            ToastUtils.showErrorCustomToast(requireContext(), "There are no Screens in Figma!")
        })
    }

    private fun handleHospitalTarrifsLo() {
        binding.hospitalTariffsLo.setOnClickListener { view ->
               startActivity(Intent(requireContext(), HospitalTariffsListActivity::class.java))
        }
    }

    private fun handleHospitalTieupsLo() {
        binding.hospitalTieupsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), HospitalTieupsActivity::class.java))
        }
    }

    private fun handleHospitalFecilitiesLo() {
        binding.hospitalFacilitiesLo.setOnClickListener { view ->
              startActivity(Intent(requireContext(), HospitalFecilitiesActivity::class.java))
        }

    }



    private fun handlePromoCouponsLo() {
        binding.promoCouponsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), PromoCouponsActivity::class.java))
        }
    }

    private fun handleTeamConnectLo() {
        binding.teamConnectLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), TeamConnectActivity::class.java))
        }
    }


    private fun handleHospitalHodsLo() {
        binding.hospitalHodsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), HospitalHodsActivity::class.java))
        }
    }

    private fun handleHospitalDoctorsLo() {
        binding.hospitalDoctorsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), HospitalDoctorsActivity::class.java))
        }
    }

    private fun handleAddUSersLo() {
        binding.addUsersLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), AddUsersActivity::class.java))
        })
    }

}