package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.authentication.LoginApiRequest

import com.iprism.ecmhealthadvisor.modals.authentication.LoginResponse
import com.iprism.ecmhealthadvisor.modals.authentication.ProfileApiRequest
import com.iprism.ecmhealthadvisor.modals.authentication.ProfileResponse
import com.iprism.ecmhealthadvisor.modals.authentication.ResendOtpApiRequest
import com.iprism.ecmhealthadvisor.repositoris.AuthenticationRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<UiState<LoginResponse>>()
    val loginResponse: LiveData<UiState<LoginResponse>> = _loginResponse

    private val _resendOtpResponse = MutableLiveData<UiState<LoginResponse>>()
    val resendOtpResponse: LiveData<UiState<LoginResponse>> = _resendOtpResponse

    private val _profileResponse = MutableLiveData<UiState<ProfileResponse>>()
    val profileResponse: LiveData<UiState<ProfileResponse>> = _profileResponse

    fun login(request: LoginApiRequest) {
        viewModelScope.launch {
            _loginResponse.value = UiState.Loading
            try {
                val response = repository.login(request)
                if (response.status) {
                    _loginResponse.value = UiState.Success(response.response)
                } else {
                    _loginResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _loginResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun resendOtp(request: ResendOtpApiRequest) {
        viewModelScope.launch {
            _resendOtpResponse.value = UiState.Loading
            try {
                val response = repository.resendOtp(request)
                if (response.status) {
                    _resendOtpResponse.value = UiState.Success(response.response)
                } else {
                    _resendOtpResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _resendOtpResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchProfileDetails(request: ProfileApiRequest) {
        viewModelScope.launch {
            _profileResponse.value = UiState.Loading
            try {
                val response = repository.fetchProfileDetails(request)
                if (response.status) {
                    _profileResponse.value = UiState.Success(response.response)
                } else {
                    _profileResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _profileResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}