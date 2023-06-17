package com.mad43.staylistaadmin.discount.data.entity

data class DiscountCode(
    val code: String? = "",
    val created_at: String?="",
    val id: Long = 0L,
    val price_rule_id: Long = 0L,
    val updated_at: String?= "",
    val usage_count: Int ?=0
)