package com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface
import com.mad43.staylistaadmin.utils.DiscountAPIState
import com.mad43.staylistaadmin.utils.PriceRuleAPIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiscountViewModel(private val repo: DiscountRepoInterface) : ViewModel() {

    private val _discountStateFlow = MutableStateFlow<DiscountAPIState>(DiscountAPIState.Loading)
    val discountStateFlow: StateFlow<DiscountAPIState> = _discountStateFlow
    private val _errorStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow

    fun getAllDiscount(id: Long) {
        viewModelScope.launch {
            val flow = repo.getAllDiscounts(id)
            flow.catch {
                _discountStateFlow.value = DiscountAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    _discountStateFlow.value = DiscountAPIState.OnSuccess(it.body()!!)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }
}