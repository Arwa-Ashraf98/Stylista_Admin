package com.mad43.staylistaadmin.product.presentation.productDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface

class ProductDetailsViewModelFactory (private val repo : RepoInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)){
            ProductDetailsViewModel(repo) as T
        }else {
            throw IllegalArgumentException("Detailed Product View model class cannot be found")
        }
    }
}