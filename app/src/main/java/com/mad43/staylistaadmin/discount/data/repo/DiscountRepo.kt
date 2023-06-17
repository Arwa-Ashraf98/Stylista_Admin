package com.mad43.staylistaadmin.discount.data.repo

import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import com.mad43.staylistaadmin.discount.domain.remot.DiscountRemoteSourceInterface
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class DiscountRepo(private val remoteSource: DiscountRemoteSourceInterface) :
    DiscountRepoInterface {
    override suspend fun getAllDiscounts(id: Long): Flow<Response<DiscountRoot>> {
        return flowOf(remoteSource.getAllDiscounts(id))
    }

    override suspend fun getDiscountById(
        priceRuleId: Long,
        discountId: Long
    ): Flow<Response<DiscountDetailsRoot>> {
        return flowOf(remoteSource.getDiscountById(priceRuleId, discountId))
    }

    override suspend fun updateDiscount(
        priceRuleId: Long,
        discountId: Long , discountDetailsRoot: DiscountDetailsRoot
    ): Flow<Response<DiscountDetailsRoot>> {
       return flowOf(remoteSource.updateDiscount(priceRuleId, discountId, discountDetailsRoot))
    }


    override suspend fun deleteDiscount(priceRuleId: Long, discountId: Long): Flow<Response<Void>> {
        return flowOf(remoteSource.deleteDiscount(priceRuleId, discountId))
    }

    override suspend fun createDiscount(priceRuleId: Long , discountDetailsRoot: DiscountDetailsRoot): Flow<Response<DiscountDetailsRoot>> {
        return flowOf(remoteSource.createDiscount(priceRuleId , discountDetailsRoot))
    }
}