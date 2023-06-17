package com.mad43.staylistaadmin.discount.domain.repo

import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DiscountRepoInterface {
    suspend fun getAllDiscounts(id: Long): Flow<Response<DiscountRoot>>
    suspend fun getDiscountById(
        priceRuleId: Long,
        discountId: Long
    ): Flow<Response<DiscountDetailsRoot>>

    suspend fun updateDiscount(
        priceRuleId: Long,
        discountId: Long,
        discountDetailsRoot: DiscountDetailsRoot
    ): Flow<Response<DiscountDetailsRoot>>

    suspend fun deleteDiscount(priceRuleId: Long, discountId: Long): Flow<Response<Void>>

    suspend fun createDiscount(priceRuleId: Long , discountDetailsRoot: DiscountDetailsRoot): Flow<Response<DiscountDetailsRoot>>

}