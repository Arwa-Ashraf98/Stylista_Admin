package com.mad43.staylistaadmin.login.data.repo

import com.mad43.staylistaadmin.login.domain.local.LocalSourceInterface
import com.mad43.staylistaadmin.login.domain.repo.LoginRepoInterface

class LoginRepo(private val localSourceInterface: LocalSourceInterface) : LoginRepoInterface {

    override fun saveShopName(shopName: String) {
        localSourceInterface.saveShopName(shopName)
    }

    override fun getShopName(): String {
        return localSourceInterface.getShopName()
    }

    override fun savePassword(password: String) {
        localSourceInterface.savePassword(password)
    }

    override fun getPassword(): String {
        return localSourceInterface.getPassword()
    }
}