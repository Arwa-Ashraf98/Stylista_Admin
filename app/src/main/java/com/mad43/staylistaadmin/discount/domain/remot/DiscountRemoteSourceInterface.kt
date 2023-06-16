package com.mad43.staylistaadmin.discount.domain.remot

import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import retrofit2.Response

interface DiscountRemoteSourceInterface {
    suspend fun getAllDiscounts(id : Long) : Response<DiscountRoot>
}