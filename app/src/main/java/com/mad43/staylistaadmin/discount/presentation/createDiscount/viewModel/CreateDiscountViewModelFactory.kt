package com.mad43.staylistaadmin.discount.presentation.createDiscount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface

@Suppress("UNCHECKED_CAST")
class CreateDiscountViewModelFactory(private val repo: DiscountRepoInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateDiscountViewModel::class.java)) {
            CreateDiscountViewModel(repo) as T
        } else {
            throw IllegalArgumentException("Create Discount View model class cannot be found")
        }
    }
}