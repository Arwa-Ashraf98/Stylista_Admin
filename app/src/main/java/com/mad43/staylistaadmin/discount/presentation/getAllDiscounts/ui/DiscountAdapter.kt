package com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mad43.staylistaadmin.databinding.ItemCouponsBinding
import com.mad43.staylistaadmin.discount.data.entity.DiscountCode

class DiscountAdapter : Adapter<DiscountAdapter.DiscountHolder>() {

    private var list: List<DiscountCode> = listOf()
    fun setList(list: List<DiscountCode>) {
        this.list = list
    }

    private var setOnDiscountClickListener: DiscountClickListener? = null


    fun setOnDiscountClickListener(setOnDiscountClickListener: DiscountClickListener) {
        this.setOnDiscountClickListener = setOnDiscountClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountHolder {
        val binding = ItemCouponsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscountHolder(binding)
    }

    override fun getItemCount() = if (list.isEmpty()) 0 else list.size

    override fun onBindViewHolder(holder: DiscountHolder, position: Int) {
        val model = list[position]
        holder.binding.apply {
            textViewCoupons.text = model.code
        }
    }

    inner class DiscountHolder(var binding: ItemCouponsBinding) : ViewHolder(binding.root) {
        init {
            binding.apply {
                textViewCoupons.setOnClickListener {
                    setOnDiscountClickListener?.onDiscountClickListener(
                        list[layoutPosition].price_rule_id,
                        list[layoutPosition].id
                    )
                }

                imageViewDeleteCoupons.setOnClickListener {
                    setOnDiscountClickListener?.onDeleteClickListener(
                        list[layoutPosition].id,
                        list[layoutPosition].price_rule_id
                    )
                }
            }
        }

    }

    interface DiscountClickListener {
        fun onDiscountClickListener(priceRuleId: Long, discountId: Long)
        fun onDeleteClickListener(discountId: Long, priceRuleId: Long)
    }
}