package com.mad43.staylistaadmin.priceRule.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.priceRule.domain.repo.PriceRuleRepoInterface
import com.mad43.staylistaadmin.utils.PriceRuleAPIState
import com.mad43.staylistaadmin.utils.ProductAPIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PriceRuleViewModel(private val repo: PriceRuleRepoInterface) : ViewModel() {
    private val _priceRuleStateFlow = MutableStateFlow<PriceRuleAPIState>(PriceRuleAPIState.Loading)
    val priceRuleStateFlow: StateFlow<PriceRuleAPIState> = _priceRuleStateFlow
    private val _errorStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow

    fun getAllPriceRule() {
        viewModelScope.launch {
            val flow = repo.getAllPriceRules()
            flow.catch {
                _priceRuleStateFlow.value = PriceRuleAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()
                    _priceRuleStateFlow.value = PriceRuleAPIState.OnSuccess(data!!)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }
}