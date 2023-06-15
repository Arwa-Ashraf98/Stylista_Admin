package com.mad43.staylistaadmin.base.data.remote

import com.mad43.staylistaadmin.product.data.entity.ImageRoot
import com.mad43.staylistaadmin.product.data.entity.Product
import com.mad43.staylistaadmin.product.data.entity.ProductModel
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitServices {
    // products request

    // 1) get all products
    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductModel>

    // 2) Delete Product
    @DELETE("products/{id}.json")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Void>

    // Get Product By Id
    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") id: Long): Response<SecondProductModel>


    @POST("products.json")
    suspend fun createProduct(@Body productModel: SecondProductModel) : Response<SecondProductModel>

    @POST("products/{id}//images.json")
    suspend fun uploadPosterImage(@Path("id") id : Long , @Body imageRoot: ImageRoot) : Response<ImageRoot>

    @PUT("products/{id}.json")
    suspend fun updateProduct(@Path("id") id : Long , @Body secondProductModel: SecondProductModel) : Response<SecondProductModel>
}