package com.mad43.staylistaadmin.discount.domain.repo

import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DiscountRepoInterface {
    suspend fun getAllDiscounts(id : Long) : Flow<Response<DiscountRoot>>
}