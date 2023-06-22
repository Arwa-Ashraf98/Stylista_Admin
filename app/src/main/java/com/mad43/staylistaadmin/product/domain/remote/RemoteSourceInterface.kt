package com.mad43.staylistaadmin.product.domain.remote

import com.mad43.staylistaadmin.product.data.entity.*
import retrofit2.Response

interface RemoteSourceInterface {
    suspend fun getAllProduct(): Response<ProductModel>
    suspend fun deleteProduct(id: Long): Response<Void>
    suspend fun getProductById(id: Long): Response<SecondProductModel>
    suspend fun createProduct(productModel: SecondProductModel): Response<SecondProductModel>
    suspend fun uploadPosterImage(id: Long, imageRoot: ImageRoot): Response<ImageRoot>
    suspend fun updateProduct(
        id: Long,
        secondProductModel: SecondProductModel
    ): Response<SecondProductModel>

    suspend fun updateQuantity(inventoryLevel: InventoryLevel): Response<InventoryLevelRoot>

}