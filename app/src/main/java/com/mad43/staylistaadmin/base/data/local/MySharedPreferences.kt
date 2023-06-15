package com.mad43.staylistaadmin.base.data.local

import android.content.Context
import android.content.SharedPreferences
import com.mad43.staylistaadmin.utils.Const

class MySharedPreferences {
    companion object {

        private lateinit var appContext : Context
        fun initSharedPreferences(context: Context){
            this.appContext = context
        }

        private fun getSharedPreferences() : SharedPreferences {
            return appContext.getSharedPreferences(Const.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        }

        fun setShopName(shopName : String){
            val editor : SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putString(Const.SHOP_NAME_KEY , shopName).apply()
        }

        fun getShopName() : String{
            return getSharedPreferences().getString(Const.SHOP_NAME_KEY,"") as String
        }

        fun setShopPassword(shopPassword : String){
            val editor : SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putString(Const.SHOP_PASSWORD_KEY , shopPassword).apply()
        }

        fun getShopPassword() : String{
            return getSharedPreferences().getString(Const.SHOP_PASSWORD_KEY,"") as String
        }
    }
}