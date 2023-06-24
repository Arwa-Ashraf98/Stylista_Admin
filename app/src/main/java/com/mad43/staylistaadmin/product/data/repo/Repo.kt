package com.mad43.staylistaadmin.product.data.repo

import com.mad43.staylistaadmin.product.data.entity.*
import com.mad43.staylistaadmin.product.domain.remote.RemoteSourceInterface
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class Repo private constructor(private val remoteSource: RemoteSourceInterface) :
    RepoInterface {

    companion object {
        @Volatile
        private var repo: Repo? = null

        fun getRepo(remoteSourceInterface: RemoteSourceInterface) = repo ?: synchronized(this) {
            val temp = Repo(remoteSourceInterface)
            repo = temp
            temp
        }
    }

    override suspend fun getAllProduct(): Flow<Response<ProductModel>> {
        return flowOf(remoteSource.getAllProduct())
    }

    override suspend fun deleteProduct(id: Long): Flow<Response<Void>> {
        return flowOf(remoteSource.deleteProduct(id))
    }

    override suspend fun getProductById(id: Long): Flow<Response<SecondProductModel>> {
        return flowOf(remoteSource.getProductById(id))
    }

    override suspend fun createProduct(productModel: SecondProductModel): Flow<Response<SecondProductModel>> {
        return flowOf(remoteSource.createProduct(productModel))
    }

    override suspend fun deleteVariant(productId: Long, variantId: Long): Flow<Response<Void>> {
        return flowOf(remoteSource.deleteVariant(productId, variantId))
    }


    override suspend fun uploadPosterImage(
        id: Long,
        imageRoot: ImageRoot
    ): Flow<Response<ImageRoot>> {
        return flowOf(remoteSource.uploadPosterImage(id, imageRoot))
    }

    override suspend fun deleteProductImage(
        productId: Long,
        imageId: Long
    ): Flow<Response<ImageRoot>> {
        return flowOf(remoteSource.deleteProductImage(productId, imageId))
    }

    override suspend fun updateProduct(
        id: Long,
        secondProductModel: SecondProductModel
    ): Flow<Response<SecondProductModel>> {
        return flowOf(remoteSource.updateProduct(id, secondProductModel))
    }

    override suspend fun updateQuantity(inventoryLevel: InventoryLevel): Flow<Response<InventoryLevelRoot>> {
        return flowOf(remoteSource.updateQuantity(inventoryLevel))
    }
}