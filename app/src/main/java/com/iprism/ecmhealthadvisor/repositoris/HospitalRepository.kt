package com.iprism.ecmhealthadvisor.repositoris
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromosApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromosApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DoctorsApiRequest

import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DoctorsApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HealthMediaApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HealthMediaApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalFacilitiesApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HospitalFacilitiesApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosApiResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TieupsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TieupsApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class HospitalRepository {


    private val apiService = EcManagersApi.ecManagersService


    suspend fun fetchHospitalHods(request: DoctorsApiRequest): DoctorsApiResponse {
        return apiService.fetchHospitalHods(request)
    }

    suspend fun fetchHospitalDoctors(request: DoctorsApiRequest): DoctorsApiResponse {
        return apiService.fetchHospitalDoctors(request)
    }

    suspend fun fetchTeamConnectEmployees(request: TeamConnectApiRequest): TeamConnectApiResponse {
        return apiService.fetchTeamConnectEmployees(request)
    }

//    suspend fun dailyReportsInsertAndView(request: DailyReportApiRequest): DailyReportApiResponse {
//        return apiService.dailyReportsInsertAndView(request)
//    }
//
//    suspend fun fetchHospitalTariffs(request: HospitalTariffsApiRequest): HospitalTariffsApiResponse {
//        return apiService.fetchHospitalTariffs(request)
//    }
//
//    suspend fun fetchSurgicalPackages(request: SurgicalPackagesApiRequest): SurgicalPackagesApiResponse {
//        return apiService.fetchSurgicalPackages(request)
//    }
//
//    suspend fun insertWhiteBoardFeedback(request: WhiteBoardFeedbackApiRequest): WhiteBoardFeedBackApiResponse {
//        return apiService.whiteBoardFeedBackInsert(request)
//    }


    suspend fun fetchTaskPerformanceDetails(request: TaskAndPerformanceApiRequest): TaskAndPerformanceApiResponse {
        return apiService.fetchTaskPerformanceDetails(request)
    }

//    suspend fun fetchContentPagesData(request: ContentPagesApiRequest): ContentPagesApiResponse {
//        return apiService.fetchContentPages(request)
//    }
//
    suspend fun fetchHospitalFacilities(request: HospitalFacilitiesApiRequest): HospitalFacilitiesApiResponse {
        return apiService.fetchHospitalFacilities(request)
    }

    suspend fun fetchHospitalTieups(request: TieupsApiRequest): TieupsApiResponse {
        return apiService.fetchHospitalTieups(request)
    }

//    suspend fun contactUs(request: ContactUsApiRequest): ContactUsApiResponse {
//        return apiService.contactUs(request)
//    }

    suspend fun fetchHealthMediaData(request: HealthMediaApiRequest): HealthMediaApiResponse {
        return apiService.fetchHealthMedia(request)
    }

    suspend fun fetchDigitalPromos(request: DigitalPromosApiRequest): DigitalPromosApiResponse {
        return apiService.fetchDigitalPromos(request)
    }

    suspend fun fetchTestimonialVideos(request: TestimonialVideosApiRequest): TestimonialVideosApiResponse {
        return apiService.fetchTestimonialVideos(request)
    }

}