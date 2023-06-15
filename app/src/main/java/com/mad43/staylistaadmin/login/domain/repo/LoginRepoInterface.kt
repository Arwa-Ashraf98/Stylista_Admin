package com.mad43.staylistaadmin.login.domain.repo

interface LoginRepoInterface {
    fun saveShopName(shopName : String)
    fun getShopName() : String
    fun savePassword(password : String)
    fun getPassword() : String
}