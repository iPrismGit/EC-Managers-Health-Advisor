package com.iprism.ecmhealthadvisor.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.activities.AddUsersActivity
import com.iprism.ecmhealthadvisor.activities.AdvisorBenefitsActivity
import com.iprism.ecmhealthadvisor.activities.DigitalBrandingsActivity
import com.iprism.ecmhealthadvisor.activities.HealthMediaActivity
import com.iprism.ecmhealthadvisor.activities.HospitalDoctorsActivity
import com.iprism.ecmhealthadvisor.activities.HospitalFecilitiesActivity
import com.iprism.ecmhealthadvisor.activities.HospitalHodsActivity
import com.iprism.ecmhealthadvisor.activities.HospitalTariffsListActivity
import com.iprism.ecmhealthadvisor.activities.HospitalTieupsActivity
import com.iprism.ecmhealthadvisor.activities.LoginActivity
import com.iprism.ecmhealthadvisor.activities.MobileContactMembersActivity
import com.iprism.ecmhealthadvisor.activities.MyTasksActivity
import com.iprism.ecmhealthadvisor.activities.ReferDiagnosticActivity
import com.iprism.ecmhealthadvisor.activities.RefferAnAdmissionActivity
import com.iprism.ecmhealthadvisor.activities.RewardsActivity
import com.iprism.ecmhealthadvisor.activities.TeamConnectActivity
import com.iprism.ecmhealthadvisor.activities.TestimonialVideosActivity
import com.iprism.ecmhealthadvisor.activities.WhiteBoardFeedBackActivity
import com.iprism.ecmhealthadvisor.adapters.BannersAdapter
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.databinding.FragmentHomeBinding
import com.iprism.ecmhealthadvisor.modals.homepagemodels.Banner
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.repositoris.HomePageRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.viewmodels.HomePageViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import com.onesignal.OneSignal
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomePageViewModel
    private lateinit var user: User
    private var playerId: String = ""
    private lateinit var userDetails: HashMap<String, String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        user = User(requireContext())
        userDetails = user.getUserDetails()
        val deviceState = OneSignal.getDeviceState()
        if (deviceState != null) {
            playerId = deviceState.userId ?: ""
            Log.d("OneSignal", "Player ID1: $playerId")
        }
      //  OneSignal.sendTags(JSONObject().put("user_type", "health_advisor"))
        val tags = JSONObject()
        tags.put("user_type", "health_advisor")
        tags.put("main_data_id", userDetails[User.MAIN_DATA_ID].toString())
        OneSignal.sendTags(tags)
        Log.d("userDetails", userDetails.toString())
        initViewModel()
        observeHomePageResponse()
        var homePageApiRequest = HomePageApiRequest(
            userDetails[User.AUTH_TOKEN].toString(),
            userDetails[User.MAIN_DATA_ID].toString(),
            playerId,
            userDetails[User.ID].toString()
        )
        viewModel.fetchHomePageBanners(homePageApiRequest)
        handleAddUSersLo()
        handleHospitalDoctorsLo()
        handleHospitalHodsLo()
        handleTeamConnectLo()
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
        handleReferAdmissionLo()
        handleServiceFeedBackLo()
        handleDigitalBrandingLo()
        handleAdvisorBenefitsLo()
        handleSocialMediaLo()
        handleRewardsLo()
        return binding.root
    }

    private fun handleRewardsLo() {
        binding.rewardsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), RewardsActivity::class.java))
        }
    }

    private fun initViewModel() {
        val repository = HomePageRepository()
        val factory = ViewModelFactory { HomePageViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[HomePageViewModel::class.java]
    }

    private fun setupTopBannersAdapter(banners: List<Banner>) {
        val adapter = BannersAdapter(banners)
        binding.topBannersSlider.setSliderAdapter(adapter)
        binding.topBannersSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.topBannersSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.topBannersSlider.scrollTimeInSec = 3
        binding.topBannersSlider.isAutoCycle = true
        binding.topBannersSlider.indicatorSelectedColor =
            ContextCompat.getColor(requireContext(), R.color.green)
        binding.topBannersSlider.indicatorUnselectedColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        binding.topBannersSlider.setIndicatorRadius(4)
        binding.topBannersSlider.startAutoCycle()
    }

    private fun setupMiddleBannersAdapter(banners: List<Banner>) {
        val adapter = BannersAdapter(banners)
        binding.middleBannersSlider.setSliderAdapter(adapter)
        binding.middleBannersSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.middleBannersSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.middleBannersSlider.scrollTimeInSec = 3
        binding.middleBannersSlider.isAutoCycle = true
        binding.middleBannersSlider.indicatorSelectedColor =
            ContextCompat.getColor(requireContext(), R.color.green)
        binding.middleBannersSlider.indicatorUnselectedColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        binding.middleBannersSlider.setIndicatorRadius(4)
        binding.middleBannersSlider.startAutoCycle()
    }

    private fun setupBottomBannersAdapter(banners: List<Banner>) {
        val adapter = BannersAdapter(banners)
        binding.bottomBannersSlider.setSliderAdapter(adapter)
        binding.bottomBannersSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.bottomBannersSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.bottomBannersSlider.scrollTimeInSec = 3
        binding.bottomBannersSlider.isAutoCycle = true
        binding.bottomBannersSlider.indicatorSelectedColor =
            ContextCompat.getColor(requireContext(), R.color.green)
        binding.bottomBannersSlider.indicatorUnselectedColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        binding.bottomBannersSlider.setIndicatorRadius(4)
        binding.bottomBannersSlider.startAutoCycle()
    }

    private fun observeHomePageResponse() {
        viewModel.homePageResponse.observe(requireContext() as LifecycleOwner) { result ->
            if (!isAdded) return@observe
            when (result) {
                is UiState.Loading -> {
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.shimmerLo.visibility = View.GONE
                    binding.mainLo.visibility = View.VISIBLE
                    if (result.data.top_banners.isNotEmpty()) {
                        setupTopBannersAdapter(result.data.top_banners)
                    } else {
                        binding.topBannersLo.visibility = View.GONE
                    }

                    if (result.data.middle_banners.isNotEmpty()) {
                        setupMiddleBannersAdapter(result.data.middle_banners)
                    } else {
                        binding.middleBannersLo.visibility = View.GONE
                    }
                    if (result.data.bottom_banners.isNotEmpty()) {
                        setupBottomBannersAdapter(result.data.bottom_banners)
                    } else {
                        binding.bottomBannersLo.visibility = View.GONE
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                    if (result.message.equals("Token not verified", true)) {
                        user.logoutUser()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun handleSocialMediaLo() {
        binding.socialMediaLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), DigitalBrandingsActivity::class.java))        }
    }

    private fun handleAdvisorBenefitsLo() {
        binding.advisorBenefitsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), AdvisorBenefitsActivity::class.java))
        }
    }

    private fun handleDigitalBrandingLo() {
        binding.digitalBrandingLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), DigitalBrandingsActivity::class.java))
        }
    }

    private fun handleServiceFeedBackLo() {
        binding.serviceFeedBackLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), WhiteBoardFeedBackActivity::class.java))
        }
    }

    private fun handleReferDiagnosticLo() {
        binding.referDiagnosticLo.setOnClickListener { view ->
            var intent = Intent(requireContext(), ReferDiagnosticActivity::class.java)
            intent.putExtra("tag", "Diagnostic Test")
            intent.putExtra("type", "diagnostic_test")
            startActivity(intent)
        }
    }

    private fun handleReferAdmissionLo() {
        binding.referAdmissionLo.setOnClickListener { view ->
            var intent = Intent(requireContext(), RefferAnAdmissionActivity::class.java)
            intent.putExtra("tag", "Hospital Admission")
            intent.putExtra("type", "hospital_admission")
            startActivity(intent)
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
            intent.putExtra("type", "whatsapp_group")
            startActivity(intent)
        }
    }

    private fun handleSocialGroupMembers() {
        binding.socialGroupMembersLo.setOnClickListener { view ->
            var value = getString(R.string.social_group_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            intent.putExtra("type", "social_group")
            startActivity(intent)
        }
    }

    private fun handleMyGroupMembersLo() {
        binding.myGroupMembersLo.setOnClickListener { view ->
            var value = getString(R.string.my_group_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            intent.putExtra("type", "my_group")
            startActivity(intent)
        }
    }

    private fun handleMobileContactMembers() {
        binding.mobileContactMembersLo.setOnClickListener { view ->
            var value = getString(R.string.mobile_contact_members)
            var intent = Intent(requireContext(), MobileContactMembersActivity::class.java)
            intent.putExtra("tag", value)
            intent.putExtra("type", "mobile_contact")
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
            var intent = Intent(requireContext(), TestimonialVideosActivity::class.java)
            intent.putExtra("type", "health_talks")
            intent.putExtra("title", "Health Talks")
            startActivity(intent)
        }
    }

    private fun handleHospitalUpdatesLo() {
        binding.hospitalUpdatesLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(requireContext(), TestimonialVideosActivity::class.java)
            intent.putExtra("type", "hospital_updates")
            intent.putExtra("title", "Hospital Updates")
            startActivity(intent)
        })
    }

    private fun handlePatientTestimonialsLo() {
        binding.patientTestimonialsLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(requireContext(), TestimonialVideosActivity::class.java)
            intent.putExtra("type", "patient_testimonials")
            intent.putExtra("title", "Patient Testimonials")
            startActivity(intent)
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