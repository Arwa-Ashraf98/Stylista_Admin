package com.mad43.staylistaadmin.discount.data.entity

data class DiscountRoot(
    val discount_codes: List<DiscountCode> ,
    val error : ErrorRoot = ErrorRoot(Errors(emptyList()))
)