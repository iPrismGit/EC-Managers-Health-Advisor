package com.iprism.ecmhealthadvisor.network

import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadApiRequest
import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadApiResponse
import com.iprism.ecmhealthadvisor.modals.addleads.LeadsApiRequest
import com.iprism.ecmhealthadvisor.modals.addleads.LeadsApiResponse
import com.iprism.ecmhealthadvisor.modals.addleads.UserDropDownsApiResponse
import com.iprism.ecmhealthadvisor.modals.authentication.LoginApiRequest
import com.iprism.ecmhealthadvisor.modals.authentication.LoginApiResponse
import com.iprism.ecmhealthadvisor.modals.authentication.ProfileApiRequest
import com.iprism.ecmhealthadvisor.modals.authentication.ProfileApiResponse
import com.iprism.ecmhealthadvisor.modals.authentication.ResendOtpApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.ContactUsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.ContactUsApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.ContentPagesApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.ContentPagesApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromosApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromosApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DoctorsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DoctorsApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HealthMediaApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HealthMediaApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalFacilitiesApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalFacilitiesApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalTariffsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalTariffsApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TieupsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TieupsApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.WhiteBoardFeedBackApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.WhiteBoardFeedbackApiRequest
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiRequest
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiResponse
import com.iprism.ecmhealthadvisor.utils.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EcManagersService {

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun login(@Body loginRequest: LoginApiRequest) : LoginApiResponse

    @POST(Constants.RESEND_OTP_ENDPOINT)
    suspend fun resendOtp(@Body resendOtpApiRequest: ResendOtpApiRequest) : LoginApiResponse

    @POST(Constants.HOME_PAGE_ENDPOINT)
    suspend fun fetchHomePageResponse(@Body homePageApiRequest: HomePageApiRequest) : HomePageApiResponse

    @GET(Constants.USER_DROP_DOWNS_ENDPOINT)
    suspend fun fetchUserDropDowns() : UserDropDownsApiResponse

    @POST(Constants.ADD_LEAD_ENDPOINT)
    suspend fun addLead(@Body addLeadApiRequest: AddLeadApiRequest) : AddLeadApiResponse

    @POST(Constants.TOTAL_LEADS_ENDPOINT)
    suspend fun fetchTotalLeads(@Body totalLeadsApiRequest: LeadsApiRequest) : LeadsApiResponse

    @POST(Constants.HEALTH_ADVISOR_REFER_ENDPOINT)
    suspend fun referAdmissionOrDiagnostic(@Body request : ReferApiRequest) : ReferApiResponse

    @POST(Constants.HOSPITAL_DOCTORS_ENDPOINT)
    suspend fun fetchHospitalDoctors(@Body doctorsApiRequest: DoctorsApiRequest) : DoctorsApiResponse

    @POST(Constants.HOSPITAL_HODS_ENDPOINT)
    suspend fun fetchHospitalHods(@Body doctorsApiRequest: DoctorsApiRequest) : DoctorsApiResponse

    @POST(Constants.TEAM_CONNECT_ENDPOINT)
    suspend fun fetchTeamConnectEmployees(@Body teamConnectApiRequest: TeamConnectApiRequest) : TeamConnectApiResponse

    @POST(Constants.TARIFFS_ENDPOINT)
    suspend fun fetchHospitalTariffs(@Body hospitalTariffsApiRequest: HospitalTariffsApiRequest) : HospitalTariffsApiResponse

    @POST(Constants.PROFILE_ENDPOINT)
    suspend fun fetchProfileDetails(@Body profileApiRequest: ProfileApiRequest) : ProfileApiResponse


    @POST(Constants.WHITEBOARD_FEEDBACK_ENDPOINT)
    suspend fun whiteBoardFeedBackInsert(@Body whiteBoardFeedbackApiRequest: WhiteBoardFeedbackApiRequest) : WhiteBoardFeedBackApiResponse

    @POST(Constants.TASK_AND_PERFORMANCE_ENDPOINT)
    suspend fun fetchTaskPerformanceDetails(@Body taskAndPerformanceApiRequest: TaskAndPerformanceApiRequest) : TaskAndPerformanceApiResponse

    @POST(Constants.CONTENT_PAGES_ENDPOINT)
    suspend fun fetchContentPages(@Body contentPagesApiRequest: ContentPagesApiRequest) : ContentPagesApiResponse

    @POST(Constants.HOSPITAL_FACILITIES_ENDPOINT)
    suspend fun fetchHospitalFacilities(@Body facilitiesApiRequest: HospitalFacilitiesApiRequest) : HospitalFacilitiesApiResponse

    @POST(Constants.HOSPITAL_TIEUPS_ENDPOINT)
    suspend fun fetchHospitalTieups(@Body tieupsApiRequest: TieupsApiRequest) : TieupsApiResponse

    @POST(Constants.CONTACT_US_ENDPOINT)
    suspend fun contactUs(@Body contactUsApiRequest: ContactUsApiRequest) : ContactUsApiResponse

    @POST(Constants.HEALTH_MEDIA_ENDPOINT)
    suspend fun fetchHealthMedia(@Body healthMediaApiRequest: HealthMediaApiRequest) : HealthMediaApiResponse

    @POST(Constants.DIGITAL_PROMOS_ENDPOINT)
    suspend fun fetchDigitalPromos(@Body digitalPromosApiRequest: DigitalPromosApiRequest) : DigitalPromosApiResponse

    @POST(Constants.TESTIMONIAL_VIDEOS_ENDPOINT)
    suspend fun fetchTestimonialVideos(@Body request : TestimonialVideosApiRequest) : TestimonialVideosApiResponse

}