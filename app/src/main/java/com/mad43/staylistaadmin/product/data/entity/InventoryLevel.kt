package com.mad43.staylistaadmin.product.data.entity

data class InventoryLevel(
    val available: Int,
    val inventory_item_id: Long,
    val location_id: Long = 85130707243,
)