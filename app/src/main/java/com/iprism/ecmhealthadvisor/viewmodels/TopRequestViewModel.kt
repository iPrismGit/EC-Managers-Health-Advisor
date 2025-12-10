package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.toprequests.ReferAnAdmissionApiRequest
import com.iprism.ecmhealthadvisor.modals.toprequests.ReferDiagnosticApiRequest
import com.iprism.ecmhealthadvisor.modals.toprequests.RequestAndReferResponse
import com.iprism.ecmhealthadvisor.repositoris.TopRequestRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class TopRequestViewModel(private var repository: TopRequestRepository) : ViewModel() {

    private val _referAdmissionResponse = MutableLiveData<UiState<RequestAndReferResponse>>()
    val referAdmissionResponse: LiveData<UiState<RequestAndReferResponse>> = _referAdmissionResponse

    private val _referDiagnosticResponse = MutableLiveData<UiState<RequestAndReferResponse>>()
    val referDiagnosticResponse: LiveData<UiState<RequestAndReferResponse>> = _referDiagnosticResponse

    fun referAdmission(request: ReferAnAdmissionApiRequest) {
        viewModelScope.launch {
            _referAdmissionResponse.value = UiState.Loading
            try {
                val response = repository.referAdmission(request)
                if (response.status) {
                    _referAdmissionResponse.value = UiState.Success(response.response)
                } else {
                    _referAdmissionResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _referAdmissionResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun referDiagnostic(request: ReferDiagnosticApiRequest) {
        viewModelScope.launch {
            _referDiagnosticResponse.value = UiState.Loading
            try {
                val response = repository.referDiagnostic(request)
                if (response.status) {
                    _referDiagnosticResponse.value = UiState.Success(response.response)
                } else {
                    _referDiagnosticResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _referDiagnosticResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}