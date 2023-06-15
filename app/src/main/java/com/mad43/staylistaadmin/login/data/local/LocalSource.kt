package com.mad43.staylistaadmin.login.data.local

import com.mad43.staylistaadmin.base.data.local.MySharedPreferences
import com.mad43.staylistaadmin.login.domain.local.LocalSourceInterface

class LocalSource : LocalSourceInterface {

    override fun saveShopName(shopName: String) {
        MySharedPreferences.setShopName(shopName)
    }

    override fun getShopName(): String {
        return MySharedPreferences.getShopPassword()
    }

    override fun savePassword(password: String) {
        MySharedPreferences.setShopPassword(shopPassword = password)
    }

    override fun getPassword(): String {
        return MySharedPreferences.getShopName()
    }
}