package com.mad43.staylistaadmin.priceRule.presentation.priceRule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.priceRule.domain.repo.PriceRuleRepoInterface

@Suppress("UNCHECKED_CAST")
class PriceRuleViewModelFactory(private val repo : PriceRuleRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PriceRuleViewModel::class.java)){
            PriceRuleViewModel(repo) as T
        }else {
            throw IllegalArgumentException("Price Rule View model class cannot be found")
        }
    }
}