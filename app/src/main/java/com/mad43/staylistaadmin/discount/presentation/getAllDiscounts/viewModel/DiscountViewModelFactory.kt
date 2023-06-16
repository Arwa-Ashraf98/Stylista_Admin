package com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface
import com.mad43.staylistaadmin.priceRule.presentation.viewModel.PriceRuleViewModel

@Suppress("UNCHECKED_CAST")
class DiscountViewModelFactory(private val repo : DiscountRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DiscountViewModel::class.java)){
            DiscountViewModel(repo) as T
        }else {
            throw IllegalArgumentException("Discount View model class cannot be found")
        }
    }
}