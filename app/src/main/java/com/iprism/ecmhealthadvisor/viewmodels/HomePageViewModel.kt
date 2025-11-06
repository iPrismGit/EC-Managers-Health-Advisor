package com.iprism.ecmhealthadvisor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageResponse
import com.iprism.ecmhealthadvisor.repositoris.HomePageRepository
import com.iprism.ecmhealthadvisor.utils.UiState
import kotlinx.coroutines.launch

class HomePageViewModel (private val repository: HomePageRepository) : ViewModel() {

    private val _homePageResponse = MutableLiveData<UiState<HomePageResponse>>()
    val homePageResponse: LiveData<UiState<HomePageResponse>> = _homePageResponse

    fun fetchHomePageBanners(request: HomePageApiRequest) {
        viewModelScope.launch {
            _homePageResponse.value = UiState.Loading
            try {
                val response = repository.fetchHomePageBanners(request)
                if (response.status) {
                    _homePageResponse.value = UiState.Success(response.response)
                } else {
                    _homePageResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _homePageResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}