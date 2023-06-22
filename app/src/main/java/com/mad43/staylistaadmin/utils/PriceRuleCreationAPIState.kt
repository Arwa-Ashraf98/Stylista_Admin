package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot

sealed class PriceRuleCreationAPIState{
    class OnSuccess(var priceRule: PriceRuleRoot) : PriceRuleCreationAPIState()
    class OnFail(val errorMessage: Throwable) : PriceRuleCreationAPIState()
    object Loading : PriceRuleCreationAPIState()
}
