package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.rewards.RewardsApiRequest
import com.iprism.ecmhealthadvisor.modals.rewards.RewardsResponse
import com.iprism.ecmhealthadvisor.repositoris.RewardsRepository
import com.iprism.ecmhealthadvisor.utils.UiState

import kotlinx.coroutines.launch

class RewardsViewModel(private val repository: RewardsRepository) : ViewModel() {

    private val _rewardsResponse = MutableLiveData<UiState<RewardsResponse>>()
    val rewardsResponse: LiveData<UiState<RewardsResponse>> = _rewardsResponse

    private val _requestResponse = MutableLiveData<UiState<RewardsResponse>>()
    val requestResponse: LiveData<UiState<RewardsResponse>> = _requestResponse

    fun fetchRewards(request: RewardsApiRequest) {
        viewModelScope.launch {
            _rewardsResponse.value = UiState.Loading
            try {
                val response = repository.fetchRewards(request)
                if (response.status) {
                    _rewardsResponse.value = UiState.Success(response.response)
                } else {
                    _rewardsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _rewardsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun requestWithdraw(request: RewardsApiRequest) {
        viewModelScope.launch {
            _requestResponse.value = UiState.Loading
            try {
                val response = repository.fetchRewards(request)
                if (response.status) {
                    _requestResponse.value = UiState.Success(response.response)
                } else {
                    _requestResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _requestResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}