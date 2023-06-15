package com.mad43.staylistaadmin.product.presentation.productDetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mad43.staylistaadmin.databinding.ItemDetailedProductImagesBinding
import com.mad43.staylistaadmin.product.data.entity.Image
import com.mad43.staylistaadmin.utils.loadImage

class ImageAdapter() : Adapter<ImageAdapter.ImageHolder>() {
    private var list = listOf<Image>()
    fun setList(list: List<Image>) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ImageHolder {
        val binding =
            ItemDetailedProductImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val imageModel = list[position]
        holder.binding.apply {
            imageViewDetailedProductItem.loadImage(imageModel.src as String)

        }
    }

    override fun getItemCount() = list.size


    inner class ImageHolder(val binding: ItemDetailedProductImagesBinding) :
        ViewHolder(binding.root) {

    }
}