package com.mad43.staylistaadmin.product.presentation.productDetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.ItemDetailedProductSizeBinding
import com.mad43.staylistaadmin.product.data.entity.Variant

class SizeAdapter() : Adapter<SizeAdapter.SizeHolder>() {
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
        val sizeModel = list[position]
        holder.binding.apply {
            textViewSize.text = sizeModel

            if (rowIndex == holder.layoutPosition) {
                textViewSize.setBackgroundColor(holder.itemView.context.getColor(R.color.primary_color))
                textViewSize.setTextColor(holder.itemView.context.getColor(R.color.white))
            } else {
                textViewSize.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
                textViewSize.setTextColor(holder.itemView.context.getColor(R.color.black))
            }
        }

    }


    override fun getItemCount() = list.size

    inner class SizeHolder(val binding: ItemDetailedProductSizeBinding) : ViewHolder(binding.root) {
        init {
            binding.apply {
                textViewSize.setOnClickListener {
                    rowIndex = layoutPosition
                    notifyDataSetChanged()
                    onSizeItemClickListener?.setOnSizeItemClickListener(
                        variantList[layoutPosition].inventory_quantity.toString(),
                        variantList[layoutPosition].inventory_item_id!!
                    )
                }
            }
        }
    }

    interface OnSizeItemClickListener {
        fun setOnSizeItemClickListener(quantity: String, inventoryItemId: Long)
    }
}