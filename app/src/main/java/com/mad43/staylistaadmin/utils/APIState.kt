package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.product.data.entity.ProductModel

sealed class APIState {
    class OnSuccess(var productModel: ProductModel) : APIState()
    class OnFail(val errorMessage: Throwable) : APIState()
    object Loading : APIState()
}
