package com.mad43.staylistaadmin.discount.data.entity

data class DiscountDetailsRoot(
    val discount_code: DiscountCode ,
    val error : ErrorRoot = ErrorRoot(Errors(emptyList()))
)