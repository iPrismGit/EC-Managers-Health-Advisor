package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.BenefitsResponse
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.HealthAdvisorBenefitsApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageResponse
import com.iprism.ecmhealthadvisor.repositoris.HealthAdvisorBenefitsRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class HealthAdvisorBenefitsViewModel (private val repository: HealthAdvisorBenefitsRepository) : ViewModel() {


    private val _benefitCategoriesResponse = MutableLiveData<UiState<List<BenefitsResponse>>>()
    val benefitCategoriesResponse: LiveData<UiState<List<BenefitsResponse>>> = _benefitCategoriesResponse

    private val _benefitSubCategoriesResponse = MutableLiveData<UiState<List<BenefitsResponse>>>()
    val benefitSubCategoriesResponse: LiveData<UiState<List<BenefitsResponse>>> = _benefitSubCategoriesResponse

    private val _benefitsBookingResponse = MutableLiveData<UiState<List<BenefitsResponse>>>()
    val benefitsBookingResponse: LiveData<UiState<List<BenefitsResponse>>> = _benefitsBookingResponse

    fun fetchHealthAdvisorBenefitCategories(request: HealthAdvisorBenefitsApiRequest) {
        viewModelScope.launch {
            _benefitCategoriesResponse.value = UiState.Loading
            try {
                val response = repository.healthAdvisorBenefits(request)
                if (response.status) {
                    _benefitCategoriesResponse.value = UiState.Success(response.response)
                } else {
                    _benefitCategoriesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _benefitCategoriesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchHealthAdvisorBenefitSubCategories(request: HealthAdvisorBenefitsApiRequest) {
        viewModelScope.launch {
            _benefitSubCategoriesResponse.value = UiState.Loading
            try {
                val response = repository.healthAdvisorBenefits(request)
                if (response.status) {
                    _benefitSubCategoriesResponse.value = UiState.Success(response.response)
                } else {
                    _benefitSubCategoriesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _benefitSubCategoriesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun healthAdvisorBenefitBooking(request: HealthAdvisorBenefitsApiRequest) {
        viewModelScope.launch {
            _benefitsBookingResponse.value = UiState.Loading
            try {
                val response = repository.healthAdvisorBenefits(request)
                if (response.status) {
                    _benefitsBookingResponse.value = UiState.Success(response.response)
                } else {
                    _benefitsBookingResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _benefitsBookingResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}