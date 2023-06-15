package com.mad43.staylistaadmin.product.presentation.getAllProduct.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.ItemProductBinding
import com.mad43.staylistaadmin.product.data.entity.Product
import com.mad43.staylistaadmin.product.data.entity.Variant
import com.mad43.staylistaadmin.utils.Helpers
import com.mad43.staylistaadmin.utils.loadImage

class ProductAdapter : Adapter<ProductAdapter.Holder>() {
    private var list: List<Product> = emptyList()
//    private var variantList = listOf<Variant>()
    private var setOnItemClickListener: SetOnItemDetailsClickListener? = null

//    fun setVariantList(variantList: List<Variant>) {
//        this.variantList = variantList
//    }

    fun setList(list: List<Product>) {
        this.list = list
    }

    fun setOnItemClickListener(setOnItemClickListener: SetOnItemDetailsClickListener) {
        this.setOnItemClickListener = setOnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val productModel = list[position]
        val variantList = productModel.variants?.size
        Log.e("TAG1", "$productModel")
        Log.e("TAG1", "$variantList")
        holder.binding.apply {
//            var date = productModel.updated_at
//            val transformedDate = Helpers.transformDate(date as String)
            val date = "updated at : ${productModel.updated_at}"
            textViewDate.text = date
            val price = "${productModel.variants?.get(0)?.price as String} EGP"
            textViewPrice.text = price
            textViewProductDepartment.text = productModel.vendor
            textViewProductName.text = productModel.title
            var totalQuantity = 0
            for(q in productModel.variants){
                totalQuantity += (q.inventory_quantity) as Int
            }
            textViewQuantity.text = totalQuantity.toString()
            Glide.with(holder.itemView.context)
                .load(productModel.image?.src)
                .error(R.drawable.error_image)
                .placeholder(R.drawable.loading)
                .into(imageViewProduct)
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            0
        } else {
            list.size
        }
    }


    inner class Holder(val binding: ItemProductBinding) : ViewHolder(binding.root) {
        init {
            binding.apply {
                btnDetails.setOnClickListener {
                    setOnItemClickListener?.onItemClickListener(list[layoutPosition].id as Long)
                }

                imageViewDelete.setOnClickListener {
                    setOnItemClickListener?.onDeleteClickListener(list[layoutPosition].id as Long)
                }
            }
        }
    }

    interface SetOnItemDetailsClickListener {
        fun onItemClickListener(id: Long)
        fun onDeleteClickListener(productId: Long)
    }
}