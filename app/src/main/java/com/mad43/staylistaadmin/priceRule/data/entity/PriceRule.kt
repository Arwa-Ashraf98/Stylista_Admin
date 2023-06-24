package com.mad43.staylistaadmin.priceRule.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRule(
    val allocation_method: String? = "across",
    val created_at: String? = "",
    val customer_selection: String? = "all",
    val ends_at: String? = "",
    val id: Long = 0L,
    val once_per_customer: Boolean? = true,
    val starts_at: String? = "",
    val target_selection: String? = "all",
    val target_type: String? = "line_item",
    val title: String? = "",
    val updated_at: String? = "",
    var usage_limit: Int? = 0,
    var value: String? = "",
    val value_type: String? = "fixed_amount"
) : Parcelable