package com.mad43.staylistaadmin.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.utils.NetworkConnectivityObserver
import com.mad43.staylistaadmin.utils.NetworkStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {

    private val networkMStatusFlow = MutableStateFlow<String>("")
    val networkStatusFlow: StateFlow<String> = networkMStatusFlow

    init {
        provideNetworkStatus()
    }

    private fun provideNetworkStatus() {

        NetworkConnectivityObserver.observeNetworkConnection().onEach { networkStatus ->
            if (networkStatus == NetworkStatus.Available) {
                networkMStatusFlow.value = NetworkStatus.Available.toString()
            }else if (networkStatus == NetworkStatus.Unavailable){
                networkMStatusFlow.value = NetworkStatus.Unavailable.toString()
            }else if(networkStatus == NetworkStatus.Losing){
                networkMStatusFlow.value = NetworkStatus.Losing.toString()
            }else {
                networkMStatusFlow.value = NetworkStatus.Lost.toString()
            }
        }.launchIn(viewModelScope)
    }
}