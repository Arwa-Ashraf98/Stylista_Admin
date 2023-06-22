package com.mad43.staylistaadmin.priceRule.domain.remote

import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import retrofit2.Response

interface PriceRuleRemoteSourceInterface {
    suspend fun deletePriceRule(id: Long) : Response<Void>
    suspend fun getAllPriceRules(): Response<PriceRuleResponse>

    suspend fun createPriceRule(priceRuleRoot: PriceRuleRoot) : Response<PriceRuleRoot>

}