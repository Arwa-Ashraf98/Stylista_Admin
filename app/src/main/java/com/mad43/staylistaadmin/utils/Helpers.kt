package com.mad43.staylistaadmin.utils

import java.text.SimpleDateFormat
import java.util.*

object Helpers {

    private const val WHOLE_DATE2 = "MMM dd, yyyy - EEE hh:mm a"
    fun transformDate(date : String) : String{
        val dateFormat = SimpleDateFormat(WHOLE_DATE2, Locale.getDefault())
        return dateFormat.parse(date)?.toString() ?: ""
    }
}