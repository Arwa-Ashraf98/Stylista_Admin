package com.mad43.staylistaadmin.product.data.entity

data class Image(
    val admin_graphql_api_id: String? = "",
    val alt: Any? = null,
    val created_at: String? = "",
    val height: Int? = 0,
    val id: Long? = 0L,
    val position: Int = 0,
    val product_id: Long? = 0L,
    val src: String? = "",
    val updated_at: String? = "",
    val variant_ids: List<Any>? = emptyList(),
    val width: Int? = 0 ,
    var attachment: String?=null,
    var metafields: List<Metafield?>? = listOf(Metafield()),
    val filename: String?=null,
    )