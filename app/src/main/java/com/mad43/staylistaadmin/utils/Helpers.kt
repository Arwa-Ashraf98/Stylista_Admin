@file:Suppress("NAME_SHADOWING")

package com.mad43.staylistaadmin.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Helpers {

    @SuppressLint("SimpleDateFormat")
    fun transformDate(date: String?): String {
        date?.let {
            if (it.isBlank()){
                return it
            }

            val dateTimeGlobalPattern  = "yyyy-MM-dd'T'HH:mm:ssXXX"
            val dayPattern = "dd/MM/yyyy"

            val format = SimpleDateFormat(dateTimeGlobalPattern , Locale.US)
            val myDate = format.parse(it)
            return SimpleDateFormat(dayPattern).format(myDate)
        }
        return ""
    }
}