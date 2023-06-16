package com.mad43.staylistaadmin.discount.data.remoteSource

import com.mad43.staylistaadmin.base.data.remote.RetrofitConnection
import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import com.mad43.staylistaadmin.discount.domain.remot.DiscountRemoteSourceInterface
import retrofit2.Response

class DiscountRemoteSource : DiscountRemoteSourceInterface {
    override suspend fun getAllDiscounts(id : Long): Response<DiscountRoot> {
        return RetrofitConnection.getServices().getAllDiscounts(id)
    }
}