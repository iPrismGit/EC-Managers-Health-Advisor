package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.notification.NotificationsApiRequest
import com.iprism.ecmhealthadvisor.modals.notification.NotificationsResponse
import com.iprism.ecmhealthadvisor.repositoris.NotificationsRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch
import java.lang.Exception

class NotificationsViewModel(private val repository: NotificationsRepository) : ViewModel() {

    private val _notificationsCountResponse = MutableLiveData<UiState<NotificationsResponse>>()
    val notificationsCountResponse: LiveData<UiState<NotificationsResponse>> = _notificationsCountResponse

    private val _notificationsResponse = MutableLiveData<UiState<NotificationsResponse>>()
    val notificationsResponse: LiveData<UiState<NotificationsResponse>> = _notificationsResponse

    fun fetchNotificationsCount(request: NotificationsApiRequest) {
        viewModelScope.launch {
            _notificationsCountResponse.value = UiState.Loading
            try {
                val response = repository.fetchNotifications(request)
                if (response.status) {
                    _notificationsCountResponse.value = UiState.Success(response.response)
                } else {
                    _notificationsCountResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _notificationsCountResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchNotifications(request: NotificationsApiRequest) {
        viewModelScope.launch {
            _notificationsResponse.value = UiState.Loading
            try {
                val response = repository.fetchNotifications(request)
                if (response.status) {
                    _notificationsResponse.value = UiState.Success(response.response)
                } else {
                    _notificationsResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _notificationsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}