package com.mad43.staylistaadmin.product.data.remote

import com.mad43.staylistaadmin.base.data.remote.RetrofitConnection
import com.mad43.staylistaadmin.product.data.entity.*
import com.mad43.staylistaadmin.product.domain.remote.RemoteSourceInterface
import retrofit2.Response

class RemoteSource private constructor() : RemoteSourceInterface {

    companion object {
        @Volatile
        private var remoteSource: RemoteSource? = null

        fun getRemoteSource() = remoteSource ?: synchronized(this) {
            val temp = RemoteSource()
            remoteSource = temp
            temp
        }
    }

    override suspend fun getAllProduct(): Response<ProductModel> {
        return RetrofitConnection.getServices().getAllProducts()
    }

    override suspend fun deleteProduct(id: Long): Response<Void> {
        return RetrofitConnection.getServices().deleteProduct(id)
    }

    override suspend fun getProductById(id: Long): Response<SecondProductModel> {
        return RetrofitConnection.getServices().getProductById(id)
    }

    override suspend fun createProduct(productModel: SecondProductModel): Response<SecondProductModel> {
        return RetrofitConnection.getServices().createProduct(productModel)
    }

    override suspend fun uploadPosterImage(id: Long, imageRoot: ImageRoot): Response<ImageRoot> {
        return RetrofitConnection.getServices().uploadPosterImage(id, imageRoot)
    }

    override suspend fun deleteProductImage(productId: Long, imageId: Long): Response<ImageRoot> {
        return RetrofitConnection.getServices().deleteImage(productId, imageId)
    }

    override suspend fun updateProduct(
        id: Long,
        secondProductModel: SecondProductModel
    ): Response<SecondProductModel> {
        return RetrofitConnection.getServices().updateProduct(id, secondProductModel)
    }

    override suspend fun deleteVariant(productId: Long, variantId: Long): Response<Void> {
        return RetrofitConnection.getServices().deleteVariant(productId, variantId)
    }

    override suspend fun updateQuantity(inventoryLevel: InventoryLevel): Response<InventoryLevelRoot> {
        return RetrofitConnection.getServices().updateQuantity(inventoryLevel)
    }
}