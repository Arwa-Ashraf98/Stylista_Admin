package com.mad43.staylistaadmin.base.presentation.ui

import android.app.Application
import com.mad43.staylistaadmin.utils.NetworkChecker
import com.mad43.staylistaadmin.utils.NetworkConnectivityObserver

class App : Application() {
    private var networkChecker: NetworkChecker? = null
    override fun onCreate() {
        super.onCreate()
        networkChecker = NetworkChecker()
        networkChecker?.initNetworkChecker(applicationContext)
        NetworkConnectivityObserver.initNetworkConnectivityObserver(applicationContext)

    }
}