package com.mad43.staylistaadmin.priceRule.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRule(
    val admin_graphql_api_id: String? = "",
    val allocation_limit: String? = null,
    val allocation_method: String? = "across",
    val created_at: String? = "",
    val customer_segment_prerequisite_ids: List<Long>? = listOf(),
    val customer_selection: String? = "all",
    val ends_at: String? = "",
    val entitled_collection_ids: List<Long>? = listOf(),
    val entitled_country_ids: List<Long>? = listOf(),
    val entitled_product_ids: List<Long>? = listOf(),
    val entitled_variant_ids: List<Long>? = listOf(),
    val id: Long = 0L,
    val once_per_customer: Boolean? = true,
    val prerequisite_collection_ids: List<Long>? = listOf(),
    val prerequisite_customer_ids: List<Long>? = listOf(),
    val prerequisite_product_ids: List<Long>? = listOf(),
    val prerequisite_subtotal_range: Int? = null,
    val prerequisite_quantity_range: Int? = null,
    val prerequisite_variant_ids: List<Long>? = listOf(),
    val starts_at: String? = "",
    val target_selection: String? = "all",
    val target_type: String? = "line_item",
    val title: String? = "",
    val updated_at: String? = "",
    val usage_limit: Int? = 0,
    val value: String? = "",
    val value_type: String? = "fixed_amount"
) : Parcelable