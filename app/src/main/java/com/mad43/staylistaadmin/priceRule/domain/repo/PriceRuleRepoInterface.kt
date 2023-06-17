package com.mad43.staylistaadmin.priceRule.domain.repo

import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PriceRuleRepoInterface {

    suspend fun getAllPriceRules() : Flow<Response<PriceRuleResponse>>
    suspend fun deletePriceRule(id: Long) : Flow<Response<Void>>

}