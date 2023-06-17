package com.mad43.staylistaadmin.priceRule.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mad43.staylistaadmin.databinding.ItemPriceRuleBinding
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.product.presentation.getAllProduct.ui.ProductAdapter

class PriceRuleAdapter() : Adapter<PriceRuleAdapter.PriceRuleHolder>() {
    private var list: List<PriceRule> = listOf()
    private var setOnPriceRuleClickListener: OnPriceRuleListener? = null

    fun setOnPriceRuleClickListener(setOnPriceRuleClickListener: OnPriceRuleListener) {
        this.setOnPriceRuleClickListener = setOnPriceRuleClickListener
    }

    fun setList(lis: List<PriceRule>) {
        this.list = lis
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceRuleHolder {
        val binding =
            ItemPriceRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PriceRuleHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            0
        } else {
            list.count()
        }
    }

    override fun onBindViewHolder(holder: PriceRuleHolder, position: Int) {
        val model = list[position]
        holder.binding.apply {
            textViewPriceRule.text = model.title
        }
    }


    inner class PriceRuleHolder(var binding: ItemPriceRuleBinding) : ViewHolder(binding.root) {
        init {
            binding.apply {
                textViewPriceRule.setOnClickListener {
                    setOnPriceRuleClickListener?.onPriceRuleClickListener(list[layoutPosition].id, list[layoutPosition])
                }

                imageViewDeletePriceRule.setOnClickListener {
                    setOnPriceRuleClickListener?.onDeletePriceRule(list[layoutPosition].id)
                }

            }
        }
    }

    interface OnPriceRuleListener {
        fun onDeletePriceRule(id: Long)
        fun onPriceRuleClickListener(id: Long , priceRule: PriceRule)
    }
}