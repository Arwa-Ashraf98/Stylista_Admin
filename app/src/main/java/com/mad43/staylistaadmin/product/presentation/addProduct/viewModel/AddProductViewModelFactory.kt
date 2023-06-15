package com.mad43.staylistaadmin.product.presentation.addProduct.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface
import com.mad43.staylistaadmin.product.presentation.getAllProduct.viewModel.ProductViewModel

class AddProductViewModelFactory(private val repoInterface: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddProductViewModel::class.java)){
            AddProductViewModel(repoInterface) as T
        }else {
            throw IllegalArgumentException("Add Product View model class cannot be found")
        }
    }
}