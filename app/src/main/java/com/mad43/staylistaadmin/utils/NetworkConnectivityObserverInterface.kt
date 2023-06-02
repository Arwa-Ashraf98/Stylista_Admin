package com.mad43.staylistaadmin.utils

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityObserverInterface {
    fun observeNetworkConnection(): Flow<NetworkStatus>
}
