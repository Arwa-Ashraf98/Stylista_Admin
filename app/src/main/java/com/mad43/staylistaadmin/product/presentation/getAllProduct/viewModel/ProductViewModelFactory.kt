package com.mad43.staylistaadmin.product.presentation.getAllProduct.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface
import com.mad43.staylistaadmin.utils.NetworkChecker

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(private val repo : RepoInterface, private val networkChecker : NetworkChecker) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
            ProductViewModel(repo , networkChecker) as T
        }else {
            throw IllegalArgumentException("Product View model class cannot be found")
        }
    }
}