package com.mad43.staylistaadmin.priceRule.data.repo

import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import com.mad43.staylistaadmin.priceRule.domain.remote.PriceRuleRemoteSourceInterface
import com.mad43.staylistaadmin.priceRule.domain.repo.PriceRuleRepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class PriceRuleRepo(private val priceRuleRemoteSourceInterface: PriceRuleRemoteSourceInterface) : PriceRuleRepoInterface {


    override suspend fun getAllPriceRules(): Flow<Response<PriceRuleResponse>> {
        return flowOf(priceRuleRemoteSourceInterface.getAllPriceRules())
    }

    override suspend fun deletePriceRule(id: Long): Flow<Response<Void>> {
        return flowOf(priceRuleRemoteSourceInterface.deletePriceRule(id))
    }

    override suspend fun createPriceRule(priceRuleRoot: PriceRuleRoot): Flow<Response<PriceRuleRoot>> {
        return flowOf(priceRuleRemoteSourceInterface.createPriceRule(priceRuleRoot))
    }
}