package com.mad43.staylistaadmin.product.data.entity

data class Metafield(
    val key: String = "new",
    val namespace: String = System.currentTimeMillis().toString(),
    val type: String = "single_line_text_field",
    val value: String = "newvalue"
)
