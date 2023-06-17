package com.mad43.staylistaadmin.discount.data.remoteSource

import com.mad43.staylistaadmin.base.data.remote.RetrofitConnection
import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import com.mad43.staylistaadmin.discount.domain.remot.DiscountRemoteSourceInterface
import retrofit2.Response

class DiscountRemoteSource : DiscountRemoteSourceInterface {
    override suspend fun getAllDiscounts(id: Long): Response<DiscountRoot> {
        return RetrofitConnection.getServices().getAllDiscounts(id)
    }

    override suspend fun getDiscountById(
        priceRuleId: Long,
        discountId: Long
    ): Response<DiscountDetailsRoot> {
        return RetrofitConnection.getServices().getPriceById(priceRuleId, discountId)
    }

    override suspend fun updateDiscount(
        priceRuleId: Long,
        discountId: Long,
        discountDetailsRoot: DiscountDetailsRoot
    ): Response<DiscountDetailsRoot> {
        return RetrofitConnection.getServices().updateDiscount(priceRuleId, discountId, discountDetailsRoot)
    }

    override suspend fun deleteDiscount(priceRuleId: Long, discountId: Long): Response<Void> {
        return RetrofitConnection.getServices().deleteDiscount(priceRuleId, discountId)
    }

    override suspend fun createDiscount(priceRuleId: Long , discountDetailsRoot: DiscountDetailsRoot) : Response<DiscountDetailsRoot> {
        return RetrofitConnection.getServices().createDiscount(priceRuleId , discountDetailsRoot)
    }
}