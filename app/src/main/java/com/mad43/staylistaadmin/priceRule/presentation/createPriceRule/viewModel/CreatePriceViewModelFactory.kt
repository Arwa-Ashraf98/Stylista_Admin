package com.mad43.staylistaadmin.priceRule.presentation.createPriceRule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.priceRule.domain.repo.PriceRuleRepoInterface

@Suppress("UNCHECKED_CAST")
class CreatePriceViewModelFactory(private val repo : PriceRuleRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreatePriceRuleViewModel::class.java)){
            CreatePriceRuleViewModel(repo) as T
        }else {
            throw IllegalArgumentException("Create Price Rule View model class cannot be found")
        }
    }
}