package com.mad43.staylistaadmin.base.presentation.ui

import android.app.Application
import com.mad43.staylistaadmin.base.data.local.MySharedPreferences
import com.mad43.staylistaadmin.utils.Const
import com.mad43.staylistaadmin.utils.NetworkChecker
import com.mad43.staylistaadmin.utils.NetworkConnectivityObserver

class App : Application() {
    private var networkChecker: NetworkChecker? = null
    override fun onCreate() {
        super.onCreate()
        networkChecker = NetworkChecker()
        networkChecker?.initNetworkChecker(this)
        NetworkConnectivityObserver.initNetworkConnectivityObserver(this)
        MySharedPreferences.initSharedPreferences(this)
        MySharedPreferences.setShopName(Const.SHOP_NAME_VALUE)
        MySharedPreferences.setShopPassword(Const.SHOP_PASSWORD_VALUE)
    }
}