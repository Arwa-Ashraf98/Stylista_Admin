@file:Suppress("NAME_SHADOWING")

package com.mad43.staylistaadmin.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Helpers {

    const val wholeDatePattern = "dd/MM/yyyy"
    const val monthDatePattern = "M/d"
    @SuppressLint("SimpleDateFormat")
    fun transformDate(date: String? , datePattern : String): String {
        date?.let {
            if (it.isBlank()){
                return it
            }

            val dateTimeGlobalPattern  = "yyyy-MM-dd'T'HH:mm:ssXXX"


            val format = SimpleDateFormat(dateTimeGlobalPattern , Locale.US)
            val myDate = format.parse(it)
            return SimpleDateFormat(datePattern).format(myDate)
        }
        return ""
    }
}