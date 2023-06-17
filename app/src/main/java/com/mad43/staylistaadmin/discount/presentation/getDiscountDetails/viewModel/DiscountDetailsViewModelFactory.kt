package com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface
import com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel.DiscountViewModel

@Suppress("UNCHECKED_CAST")
class DiscountDetailsViewModelFactory(private val repo : DiscountRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DiscountDetailsViewModel::class.java)){
            DiscountDetailsViewModel(repo) as T
        }else {
            throw IllegalArgumentException("Discount Details View model class cannot be found")
        }
    }
}