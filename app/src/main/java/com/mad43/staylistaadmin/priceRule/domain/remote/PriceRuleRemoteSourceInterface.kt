package com.mad43.staylistaadmin.priceRule.domain.remote

import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import retrofit2.Response

interface PriceRuleRemoteSourceInterface {
    suspend fun deletePriceRule(id: Long) : Response<Void>
    suspend fun getAllPriceRules(): Response<PriceRuleResponse>
}