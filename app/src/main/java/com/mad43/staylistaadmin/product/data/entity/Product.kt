package com.mad43.staylistaadmin.product.data.entity

data class Product(
    val created_at: String? = "",
    val handle: String? = "",
    val id: Long? = 0L,
    val body_html : String? = "",
    val image: Image? = Image(
        "",
        null,
        "",
        0,
        0L,
        0,
        0,
        "",
        "",
        emptyList(),
        0
    ),
    val images: List<Image>? = emptyList(),
    val options: List<Option>? = emptyList(),
    var product_type: String? = "",
    val published_at: String? = "",
    val published_scope: String? = "",
    val status: String? = "active",
    val tags: String? = "",
    val template_suffix: Any? = null,
    var title: String? = "",
    val updated_at: String? = "",
    val variants: List<Variant>? = emptyList(),
    var vendor: String? = ""
)