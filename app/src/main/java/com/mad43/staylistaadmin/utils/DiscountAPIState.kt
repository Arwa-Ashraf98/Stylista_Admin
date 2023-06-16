package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import com.mad43.staylistaadmin.product.data.entity.ProductModel

sealed class DiscountAPIState  {
    class OnSuccess(var discount: DiscountRoot) : DiscountAPIState()
    class OnFail(val errorMessage: Throwable) : DiscountAPIState()
    object Loading : DiscountAPIState()
}