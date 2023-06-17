package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.product.data.entity.ProductModel

sealed class APIState {
    open class OnSuccess(var productModel: ProductModel) : APIState()

    class OnFail(val errorMessage: Throwable) : APIState()
    object Loading : APIState()
}

sealed class Test {
    data class Loaded<out T>(val users: T) : Test()
}