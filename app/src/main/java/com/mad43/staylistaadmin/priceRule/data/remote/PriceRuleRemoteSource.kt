package com.mad43.staylistaadmin.priceRule.data.remote

import com.mad43.staylistaadmin.base.data.remote.RetrofitConnection
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import com.mad43.staylistaadmin.priceRule.domain.remote.PriceRuleRemoteSourceInterface
import retrofit2.Response

class PriceRuleRemoteSource : PriceRuleRemoteSourceInterface {
    override suspend fun deletePriceRule(id: Long): Response<Void> {
        return RetrofitConnection.getServices().deletePriceRule(id)
    }

    override suspend fun getAllPriceRules(): Response<PriceRuleResponse> {
        return RetrofitConnection.getServices().getAllPriceRule()
    }
}