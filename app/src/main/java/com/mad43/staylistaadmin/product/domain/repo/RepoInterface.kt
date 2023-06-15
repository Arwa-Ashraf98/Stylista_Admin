package com.mad43.staylistaadmin.product.domain.repo

import com.mad43.staylistaadmin.product.data.entity.ImageRoot
import com.mad43.staylistaadmin.product.data.entity.Product
import com.mad43.staylistaadmin.product.data.entity.ProductModel
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {

    suspend fun getAllProduct(): Flow<Response<ProductModel>>
    suspend fun deleteProduct(id : Long) :Flow< Response<Void>>
    suspend fun getProductById(id : Long) : Flow<Response<SecondProductModel>>
    suspend fun createProduct(productModel: SecondProductModel) : Flow<Response<SecondProductModel>>

    suspend fun uploadPosterImage(id: Long , imageRoot: ImageRoot): Flow<Response<ImageRoot>>
    suspend fun updateProduct(id : Long , secondProductModel: SecondProductModel) : Flow<Response<SecondProductModel>>
}