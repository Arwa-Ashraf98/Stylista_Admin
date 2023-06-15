package com.mad43.staylistaadmin.login.domain.local

interface LocalSourceInterface {
    fun saveShopName(shopName : String)
    fun getShopName() : String
    fun savePassword(password : String)
    fun getPassword() : String
}