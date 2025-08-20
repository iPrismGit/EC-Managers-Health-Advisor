package com.iprism.ecmhealthadvisor.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        handleRewardsLo()
        handleHospitalFecilitiesLo()
        handleHospitalTieupsLo()
        handleHospitalTarrifsLo()
        handleHospitalUpdatesLo()
        handlePatientTestimonialsLo()
        handleHealthTalks()
        handleHealthMedia()
        return binding.root
    }


    private fun handleHealthMedia() {
        binding.healthMediaLo.setOnClickListener { view ->
         //   startActivity(Intent(requireContext(), HealthMediaActivity::class.java))
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
         //   startActivity(Intent(requireContext(), HospitalTariffsListActivity::class.java))
        }
    }

    private fun handleHospitalTieupsLo() {
        binding.hospitalTieupsLo.setOnClickListener { view ->
           // startActivity(Intent(requireContext(), HospitalTieupsActivity::class.java))
        }
    }

    private fun handleHospitalFecilitiesLo() {
        binding.hospitalFacilitiesLo.setOnClickListener { view ->
          //  startActivity(Intent(requireContext(), HospitalFecilitiesActivity::class.java))
        }

    }

    private fun handleRewardsLo() {
        binding.rewardsLo.setOnClickListener { view ->
          //  startActivity(Intent(requireContext(), RewardsActivity::class.java))
        }
    }

    private fun handlePromoCouponsLo() {
        binding.promoCouponsLo.setOnClickListener { view ->
           // startActivity(Intent(requireContext(), PromoCouponsActivity::class.java))
        }
    }

    private fun handleTeamConnectLo() {
        binding.teamConnectLo.setOnClickListener { view ->
        //    startActivity(Intent(requireContext(), TeamConnectActivity::class.java))
        }
    }



    private fun handleHospitalHodsLo() {
        binding.hospitalHodsLo.setOnClickListener { view ->
          //  startActivity(Intent(requireContext(), HospitalHodsActivity::class.java))
        }
    }

    private fun handleHospitalDoctorsLo() {
        binding.hospitalDoctorsLo.setOnClickListener { view ->
         //   startActivity(Intent(requireContext(), HospitalDoctorsActivity::class.java))
        }
    }



    private fun handleAddUSersLo() {
        binding.addUsersLo.setOnClickListener(View.OnClickListener {
           // startActivity(Intent(requireContext(), AddUsersActivity::class.java))
        })
    }

}