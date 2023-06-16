package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import com.mad43.staylistaadmin.product.data.entity.ProductModel

sealed class PriceRuleAPIState{
    class OnSuccess(var priceRule: PriceRuleResponse) : PriceRuleAPIState()
    class OnFail(val errorMessage: Throwable) : PriceRuleAPIState()
    object Loading : PriceRuleAPIState()
}
