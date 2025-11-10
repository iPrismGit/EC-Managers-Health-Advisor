package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DoctorsApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DoctorsResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HealthMediaApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.HealthMediaResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TaskAndPerformanceResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TeamConnectResponse
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosApiRequest
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TestimonialVideosResponse
import com.iprism.ecmhealthadvisor.repositoris.HospitalRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class HospitalViewModel(private val repository: HospitalRepository) : ViewModel() {

    private val _hospitalHodsResponse = MutableLiveData<UiState<DoctorsResponse>>()
    val hospitalHodsResponse: LiveData<UiState<DoctorsResponse>> = _hospitalHodsResponse

    private val _doctorsResponse = MutableLiveData<UiState<DoctorsResponse>>()
    val doctorsResponse: LiveData<UiState<DoctorsResponse>> = _doctorsResponse

    private val _teamConnectResponse = MutableLiveData<UiState<TeamConnectResponse>>()
    val teamConnectResponse: LiveData<UiState<TeamConnectResponse>> = _teamConnectResponse


//    private val _hospitalTariffsResponse = MutableLiveData<UiState<HospitalTariffsResponse>>()
//    val hospitalTariffsResponse: LiveData<UiState<HospitalTariffsResponse>> = _hospitalTariffsResponse

//    private val _whiteBoardFeedbackResponse = MutableLiveData<UiState<WhiteBoardFeedBackResponse>>()
//    val whiteBoardFeedbackResponse : LiveData<UiState<WhiteBoardFeedBackResponse>> = _whiteBoardFeedbackResponse

    private val _taskPerformanceDetailsResponse = MutableLiveData<UiState<TaskAndPerformanceResponse>>()
    val taskPerformanceDetailsResponse : LiveData<UiState<TaskAndPerformanceResponse>> = _taskPerformanceDetailsResponse

//    private val _contentPagesResponse = MutableLiveData<UiState<ContentPagesResponse>>()
//    val contentPagesResponse : LiveData<UiState<ContentPagesResponse>> = _contentPagesResponse
//
//    private val _facilitiesResponse = MutableLiveData<UiState<HospitalFacilitiesResponse>>()
//    val facilitiesResponse : LiveData<UiState<HospitalFacilitiesResponse>> = _facilitiesResponse
//
//    private val _tieupsResponse = MutableLiveData<UiState<TieupsResponse>>()
//    val tieupsResponse : LiveData<UiState<TieupsResponse>> = _tieupsResponse
//
//    private val _contactUsResponse = MutableLiveData<UiState<ContactUsResponse>>()
//    val contactUsResponse : LiveData<UiState<ContactUsResponse>> = _contactUsResponse
//
//    private val _contactUsInsertResponse = MutableLiveData<UiState<ContactUsResponse>>()
//    val contactUsInsertResponse : LiveData<UiState<ContactUsResponse>> = _contactUsInsertResponse

    private val _healthMediaResponse = MutableLiveData<UiState<HealthMediaResponse>>()
    val healthMediaResponse : LiveData<UiState<HealthMediaResponse>> = _healthMediaResponse

//    private val _digitalPromosResponse = MutableLiveData<UiState<DigitalPromosResponse>>()
//    val digitalPromosResponse : LiveData<UiState<DigitalPromosResponse>> = _digitalPromosResponse

    private val _testimonialVideosResponse = MutableLiveData<UiState<TestimonialVideosResponse>>()
    val testimonialVideosResponse : LiveData<UiState<TestimonialVideosResponse>> = _testimonialVideosResponse

    fun fetchHospitalHods(request: DoctorsApiRequest) {
        viewModelScope.launch {
            _hospitalHodsResponse.value = UiState.Loading
            try {
                val response = repository.fetchHospitalHods(request)
                if (response.status) {
                    _hospitalHodsResponse.value = UiState.Success(response.response)
                } else {
                    _hospitalHodsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _hospitalHodsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchHospitalDoctors(request: DoctorsApiRequest) {
        viewModelScope.launch {
            _doctorsResponse.value = UiState.Loading
            try {
                val response = repository.fetchHospitalDoctors(request)
                if (response.status) {
                    _doctorsResponse.value = UiState.Success(response.response)
                } else {
                    _doctorsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _doctorsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchTeamConnectEmployees(request: TeamConnectApiRequest) {
        viewModelScope.launch {
            _teamConnectResponse.value = UiState.Loading
            try {
                val response = repository.fetchTeamConnectEmployees(request)
                if (response.status) {
                    _teamConnectResponse.value = UiState.Success(response.response)
                } else {
                    _teamConnectResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _teamConnectResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

//    fun fetchHospitalTariffs(request: HospitalTariffsApiRequest) {
//        viewModelScope.launch {
//            _hospitalTariffsResponse.value = UiState.Loading
//            try {
//                val response = repository.fetchHospitalTariffs(request)
//                if (response.status) {
//                    _hospitalTariffsResponse.value = UiState.Success(response.response)
//                } else {
//                    _hospitalTariffsResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _hospitalTariffsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }

//    fun insertWhiteBoardFeedback(request: WhiteBoardFeedbackApiRequest) {
//        viewModelScope.launch {
//            _whiteBoardFeedbackResponse.value = UiState.Loading
//            try {
//                val response = repository.insertWhiteBoardFeedback(request)
//                if (response.status) {
//                    _whiteBoardFeedbackResponse.value = UiState.Success(response.response)
//                } else {
//                    _whiteBoardFeedbackResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _whiteBoardFeedbackResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }

    fun fetchTaskPerformanceDetails(request: TaskAndPerformanceApiRequest) {
        viewModelScope.launch {
            _taskPerformanceDetailsResponse.value = UiState.Loading
            try {
                val response = repository.fetchTaskPerformanceDetails(request)
                if (response.status) {
                    _taskPerformanceDetailsResponse.value = UiState.Success(response.response)
                } else {
                    _taskPerformanceDetailsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _taskPerformanceDetailsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

//    fun fetchContentPagesData(request: ContentPagesApiRequest) {
//        viewModelScope.launch {
//            _contentPagesResponse.value = UiState.Loading
//            try {
//                val response = repository.fetchContentPagesData(request)
//                if (response.status) {
//                    _contentPagesResponse.value = UiState.Success(response.response)
//                } else {
//                    _contentPagesResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _contentPagesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }
//
//    fun fetchHospitalFacilities(request: HospitalFacilitiesApiRequest) {
//        viewModelScope.launch {
//            _facilitiesResponse.value = UiState.Loading
//            try {
//                val response = repository.fetchHospitalFacilities(request)
//                if (response.status) {
//                    _facilitiesResponse.value = UiState.Success(response.response)
//                } else {
//                    _facilitiesResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _facilitiesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }
//
//    fun fetchHospitalTieups(request: TieupsApiRequest) {
//        viewModelScope.launch {
//            _tieupsResponse.value = UiState.Loading
//            try {
//                val response = repository.fetchHospitalTieups(request)
//                if (response.status) {
//                    _tieupsResponse.value = UiState.Success(response.response)
//                } else {
//                    _tieupsResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _tieupsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }
//
//    fun contactUs(request: ContactUsApiRequest) {
//        viewModelScope.launch {
//            _contactUsResponse.value = UiState.Loading
//            try {
//                val response = repository.contactUs(request)
//                if (response.status) {
//                    _contactUsResponse.value = UiState.Success(response.response)
//                } else {
//                    _contactUsResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _contactUsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }
//
//    fun contactUsInsert(request: ContactUsApiRequest) {
//        viewModelScope.launch {
//            _contactUsInsertResponse.value = UiState.Loading
//            try {
//                val response = repository.contactUs(request)
//                if (response.status) {
//                    _contactUsInsertResponse.value = UiState.Success(response.response)
//                } else {
//                    _contactUsInsertResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _contactUsInsertResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }

    fun fetchHealthMediaData(request: HealthMediaApiRequest) {
        viewModelScope.launch {
            _healthMediaResponse.value = UiState.Loading
            try {
                val response = repository.fetchHealthMediaData(request)
                if (response.status) {
                    _healthMediaResponse.value = UiState.Success(response.response)
                } else {
                    _healthMediaResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _healthMediaResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

//    fun fetchDigitalPromos(request: DigitalPromosApiRequest) {
//        viewModelScope.launch {
//            _digitalPromosResponse.value = UiState.Loading
//            try {
//                val response = repository.fetchDigitalPromos(request)
//                if (response.status) {
//                    _digitalPromosResponse.value = UiState.Success(response.response)
//                } else {
//                    _digitalPromosResponse.value = UiState.Error(response.message ?: "Something went wrong")
//                }
//            } catch (e: Exception) {
//                _digitalPromosResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }


    fun fetchTestimonialVideos(request: TestimonialVideosApiRequest) {
        viewModelScope.launch {
            _testimonialVideosResponse.value = UiState.Loading
            try {
                val response = repository.fetchTestimonialVideos(request)
                if (response.status) {
                    _testimonialVideosResponse.value = UiState.Success(response.response)
                } else {
                    _testimonialVideosResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _testimonialVideosResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}