package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.product.data.entity.Product
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel

sealed class ProductAPIState{
    class OnSuccess(var product: SecondProductModel) : ProductAPIState()
    class OnFail(val errorMessage: Throwable) : ProductAPIState()
    object Loading : ProductAPIState()
}
