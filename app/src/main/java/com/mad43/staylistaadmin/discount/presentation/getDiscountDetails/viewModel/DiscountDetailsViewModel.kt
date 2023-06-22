package com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import com.mad43.staylistaadmin.utils.DiscountDetailsAPIState
import com.mad43.staylistaadmin.utils.PriceRuleCreationAPIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DiscountDetailsViewModel(private val repo: DiscountRepoInterface) : ViewModel() {

    private val _discountDetailsStateFlow =
        MutableStateFlow<DiscountDetailsAPIState>(DiscountDetailsAPIState.Loading)
    val discountDetailsStateFlow: StateFlow<DiscountDetailsAPIState> = _discountDetailsStateFlow
    private val _errorStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow
    private val _discountUpdateStateFlow =
        MutableStateFlow<DiscountDetailsAPIState>(DiscountDetailsAPIState.Loading)
    val discountUpdateStateFlow: StateFlow<DiscountDetailsAPIState> = _discountUpdateStateFlow
    private val _priceRuleStateFlow =
        MutableStateFlow<PriceRuleCreationAPIState>(PriceRuleCreationAPIState.Loading)
    val priceRuleStateFlow: StateFlow<PriceRuleCreationAPIState> = _priceRuleStateFlow

    fun updateDiscount(
        priceRuleId: Long, discountId: Long, discountDetailsRoot: DiscountDetailsRoot
    ) {
        viewModelScope.launch {
            val flow = repo.updateDiscount(priceRuleId, discountId, discountDetailsRoot)
            flow.catch {
                _discountUpdateStateFlow.value = DiscountDetailsAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()?.discount_code!!
                    _discountUpdateStateFlow.value = DiscountDetailsAPIState.OnSuccess(data)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }

    fun updatePriceRule(id: Long, priceRuleRoot: PriceRuleRoot) {
        viewModelScope.launch {
            val flow = repo.updatePriceRuleBy(priceRuleId = id, priceRuleRoot)
            flow.catch {
                _priceRuleStateFlow.value = PriceRuleCreationAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()
                    _priceRuleStateFlow.value = PriceRuleCreationAPIState.OnSuccess(data!!)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }

    fun getPriceRuleById(id: Long, priceRuleRoot: PriceRuleRoot) {
        viewModelScope.launch {
            val flow = repo.getPriceRuleById(priceRuleId = id)
            flow.catch {
                _priceRuleStateFlow.value = PriceRuleCreationAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()
                    _priceRuleStateFlow.value = PriceRuleCreationAPIState.OnSuccess(data!!)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }


    fun getDiscountById(priceRuleId: Long, discountId: Long) {
        viewModelScope.launch {
            val flow = repo.getDiscountById(priceRuleId, discountId)
            flow.catch {
                _discountDetailsStateFlow.value = DiscountDetailsAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()?.discount_code!!
                    _discountDetailsStateFlow.value = DiscountDetailsAPIState.OnSuccess(data)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }


}