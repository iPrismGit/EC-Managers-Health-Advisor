package com.iprism.ecmhealthadvisor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.FragmentProfileBinding
import com.iprism.ecmhealthadvisor.modals.authentication.ProfileApiRequest
import com.iprism.ecmhealthadvisor.repositoris.AuthenticationRepository
import com.iprism.ecmhealthadvisor.utils.Constants
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.viewmodels.AuthenticationViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        user = User(requireContext())
        userDetails = user.getUserDetails()
        binding.profileIv.borderColor = ContextCompat.getColor(requireContext(), R.color.green)
        binding.profileIv.borderWidth = 4
        initViewModel()
        observeProfileResponse()
        var profileApiRequest = ProfileApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.ID].toString()
        )
        authenticationViewModel.fetchProfileDetails(profileApiRequest)
        return binding.root
    }

    private fun initViewModel() {
        val repository = AuthenticationRepository()
        val factory = ViewModelFactory { AuthenticationViewModel(repository) }
        authenticationViewModel =
            ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
    }

    private fun observeProfileResponse() {
        authenticationViewModel.profileResponse.observe(requireContext() as LifecycleOwner) { result ->
            if (!isAdded) return@observe
            when (result) {
                is UiState.Loading -> {
                    binding.shommerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.shommerLo.visibility = View.GONE
                    binding.mainLo.visibility = View.VISIBLE
                    binding.nameTxt.text = result.data.profile.name
                    binding.contactNumberTxt.text = "+91 " + result.data.profile.mobile
                    binding.emailTxt.text = result.data.profile.email
                    binding.genderTxt.text = result.data.profile.gender_name
                    binding.dateOfBirthTxt.text = result.data.profile.dob
                    if (result.data.profile.blood_group_name != null && result.data.profile.blood_group_name.isNotEmpty()) {
                        binding.bloodGroupLo.visibility = View.VISIBLE
                        binding.bloodGroupTxt.text = result.data.profile.blood_group_name
                    } else {
                        binding.bloodGroupLo.visibility = View.GONE
                    }

                    if (result.data.profile.image.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(Constants.IMAGES_URL + result.data.profile.image).error(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.customer_image
                                )
                            ).into(binding.profileIv)
                    } else {
                        binding.profileIv.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.customer_image
                            )
                        )
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.shommerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                }
            }
        }
    }


}