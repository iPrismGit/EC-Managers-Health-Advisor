package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.addleads.UserDropDownsResponse
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiRequest
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferResponse
import com.iprism.ecmhealthadvisor.repositoris.LeadsRepository
import com.iprism.ecmhealthadvisor.repositoris.ReferRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class ReferViewModel(private val repository: ReferRepository) : ViewModel() {

    private val _referResponse = MutableLiveData<UiState<ReferResponse>>()
    val referResponse: LiveData<UiState<ReferResponse>> = _referResponse

    fun referAdmissionOrDiagnostic(request: ReferApiRequest) {
        viewModelScope.launch {
            _referResponse.value = UiState.Loading
            try {
                val response = repository.referAdmissionOrDiagnostic(request)
                if (response.status) {
                    _referResponse.value = UiState.Success(response.response)
                } else {
                    _referResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _referResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}