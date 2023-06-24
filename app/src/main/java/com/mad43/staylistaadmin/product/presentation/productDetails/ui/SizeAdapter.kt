package com.mad43.staylistaadmin.product.presentation.productDetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.ItemDetailedProductSizeBinding
import com.mad43.staylistaadmin.product.data.entity.Variant
import com.mad43.staylistaadmin.utils.Helpers

class SizeAdapter : Adapter<SizeAdapter.SizeHolder>() {
    private var list = listOf<String>()
    private var onSizeItemClickListener: OnSizeItemClickListener? = null
    private var variantList = listOf<Variant>()

    companion object {
        private var rowIndex = -1
    }

    fun setOnItemSizeClickListener(onSizeItemClickListener: OnSizeItemClickListener) {
        this.onSizeItemClickListener = onSizeItemClickListener
    }

    init {
        rowIndex = -1
        notifyDataSetChanged()
    }

    fun setList(list: List<String>) {
        this.list = list
    }

    fun setVariantList(variantList: List<Variant>) {
        this.variantList = variantList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeHolder {
        val binding = ItemDetailedProductSizeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SizeHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeHolder, position: Int) {
        val model = variantList[position]
        holder.binding.apply {
            textViewSize.text = model.option1
            textViewColor.text = model.option2
            val price = "Price : ${model.price} EGP"
            textViewVariantPrice.text = price
            val date = Helpers.transformDate(model.updated_at, Helpers.monthDatePattern)
            textViewUpdatedDate.text = date
            if (rowIndex == holder.layoutPosition) {
                cardLayout.strokeColor = holder.itemView.context.getColor(R.color.primary_color)
                cardLayout.strokeWidth = 1
            } else {
                cardLayout.strokeColor = holder.itemView.context.getColor(R.color.transparent1)
                cardLayout.strokeWidth = 0
            }
        }

    }


    override fun getItemCount() = list.size

    inner class SizeHolder(val binding: ItemDetailedProductSizeBinding) : ViewHolder(binding.root) {
        init {
            binding.apply {
                cardLayout.setOnClickListener {
                    rowIndex = layoutPosition
                    notifyDataSetChanged()
                    onSizeItemClickListener?.setOnSizeItemClickListener(
                        variantList[layoutPosition].inventory_quantity.toString(),
                        variantList[layoutPosition].inventory_item_id!!,
                    )
                }

                btnDeleteVariant.setOnClickListener {
                    onSizeItemClickListener?.setOnDeleteVariantClickListener(
                        variantList[layoutPosition].product_id!!,
                        variantList[layoutPosition].id!!
                    )
                }
            }
        }
    }

    interface OnSizeItemClickListener {
        fun setOnSizeItemClickListener(
            quantity: String,
            inventoryItemId: Long,
        )

        fun setOnDeleteVariantClickListener(productId: Long, variantId: Long)
    }
}