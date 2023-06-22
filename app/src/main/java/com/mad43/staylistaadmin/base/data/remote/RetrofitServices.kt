package com.mad43.staylistaadmin.base.data.remote

import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.entity.DiscountRoot
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleResponse
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import com.mad43.staylistaadmin.product.data.entity.ImageRoot
import com.mad43.staylistaadmin.product.data.entity.ProductModel
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel
import com.mad43.staylistaadmin.product.data.entity.InventoryLevel
import com.mad43.staylistaadmin.product.data.entity.InventoryLevelRoot
import retrofit2.Response
import retrofit2.http.*

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
    suspend fun createProduct(@Body productModel: SecondProductModel): Response<SecondProductModel>

    @POST("products/{id}//images.json")
    suspend fun uploadPosterImage(
        @Path("id") id: Long,
        @Body imageRoot: ImageRoot
    ): Response<ImageRoot>

    @PUT("products/{id}.json")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body secondProductModel: SecondProductModel
    ): Response<SecondProductModel>

    // price rule
    @POST("price_rules.json")
    suspend fun createPriceRule(@Body priceRuleRoot: PriceRuleRoot): Response<PriceRuleRoot>

    @GET("price_rules.json")
    suspend fun getAllPriceRule(): Response<PriceRuleResponse>

    @GET("price_rules/{id}/discount_codes.json")
    suspend fun getAllDiscounts(@Path("id") id: Long): Response<DiscountRoot>

    @GET("price_rules/{price_rule_id}/discount_codes/{discount_id}.json")
    suspend fun getPriceById(
        @Path("price_rule_id") priceRuleId: Long,
        @Path("discount_id") discountId: Long
    ): Response<DiscountDetailsRoot>

    @DELETE("price_rules/{id}.json")
    suspend fun deletePriceRule(@Path("id") id: Long): Response<Void>

    @DELETE("price_rules/{price_rule_id}/discount_codes/{discount_id}.json")
    suspend fun deleteDiscount(
        @Path("price_rule_id") priceRuleId: Long,
        @Path("discount_id") discountId: Long
    ): Response<Void>

    @PUT("price_rules/{price_rule_id}/discount_codes/{discount_id}.json")
    suspend fun updateDiscount(
        @Path("price_rule_id") priceRuleId: Long,
        @Path("discount_id") discountId: Long,
        @Body discountDetailsRoot: DiscountDetailsRoot
    ): Response<DiscountDetailsRoot>

    @POST("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun createDiscount(
        @Path("price_rule_id") priceRuleId: Long,
        @Body discountDetailsRoot: DiscountDetailsRoot
    ): Response<DiscountDetailsRoot>

    @POST("price_rules/{id}.json")
    suspend fun getPriceRuleById(@Path("id") priceRuleId: Long): Response<PriceRuleRoot>

    @PUT("price_rules/{id}.json")
    suspend fun updatePriceRule(
        @Path("id") priceRuleId: Long,
        @Body priceRuleRoot: PriceRuleRoot
    ): Response<PriceRuleRoot>


    // variants
    @POST("inventory_levels/set.json")
    suspend fun updateQuantity(@Body inventoryLevel: InventoryLevel): Response<InventoryLevelRoot>

}