package com.mad43.staylistaadmin.discount.data.repo

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
}