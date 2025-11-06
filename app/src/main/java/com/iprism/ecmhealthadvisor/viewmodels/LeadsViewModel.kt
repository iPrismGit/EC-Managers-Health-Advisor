package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadApiRequest
import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadResponse
import com.iprism.ecmhealthadvisor.modals.addleads.UserDropDownsResponse
import com.iprism.ecmhealthadvisor.repositoris.LeadsRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class LeadsViewModel(private val repository: LeadsRepository) : ViewModel() {

    private val _userDropDownsResponse = MutableLiveData<UiState<UserDropDownsResponse>>()
    val userDropDownsResponse: LiveData<UiState<UserDropDownsResponse>> = _userDropDownsResponse

    private val _addLeadResponse = MutableLiveData<UiState<AddLeadResponse>>()
    val addLeadResponse: LiveData<UiState<AddLeadResponse>> = _addLeadResponse

    fun fetchUserDropDowns() {
        viewModelScope.launch {
            _userDropDownsResponse.value = UiState.Loading
            try {
                val response = repository.fetchUserDropDowns()
                if (response.status) {
                    _userDropDownsResponse.value = UiState.Success(response.response)
                } else {
                    _userDropDownsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _userDropDownsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun addLead(request: AddLeadApiRequest) {
        viewModelScope.launch {
            _addLeadResponse.value = UiState.Loading
            try {
                val response = repository.addLead(request)
                if (response.status) {
                    _addLeadResponse.value = UiState.Success(response.response)
                } else {
                    _addLeadResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _addLeadResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}