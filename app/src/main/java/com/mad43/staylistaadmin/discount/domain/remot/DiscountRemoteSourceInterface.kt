package com.mad43.staylistaadmin.discount.domain.remot

import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DiscountRemoteSourceInterface {
    suspend fun getAllDiscounts(id : Long) : Response<DiscountRoot>

    suspend fun getDiscountById(priceRuleId : Long , discountId : Long) : Response<DiscountDetailsRoot>
    suspend fun updateDiscount(priceRuleId : Long , discountId : Long , discountDetailsRoot: DiscountDetailsRoot) : Response<DiscountDetailsRoot>
    suspend fun deleteDiscount(priceRuleId : Long , discountId : Long) : Response<Void>

    suspend fun createDiscount(priceRuleId: Long , discountDetailsRoot: DiscountDetailsRoot) : Response<DiscountDetailsRoot>

}