package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.discount.data.entity.DiscountCode
import com.mad43.staylistaadmin.product.data.entity.ProductModel

sealed class DiscountDetailsAPIState{
    class OnSuccess(var discount: DiscountCode) : DiscountDetailsAPIState()
    class OnFail(val errorMessage: Throwable) : DiscountDetailsAPIState()
    object Loading : DiscountDetailsAPIState()

}
