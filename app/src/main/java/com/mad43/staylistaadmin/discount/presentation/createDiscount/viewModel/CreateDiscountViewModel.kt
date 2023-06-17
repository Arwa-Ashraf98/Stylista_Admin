package com.mad43.staylistaadmin.discount.presentation.createDiscount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.domain.repo.DiscountRepoInterface
import com.mad43.staylistaadmin.utils.Const
import com.mad43.staylistaadmin.utils.DiscountDetailsAPIState
import com.mad43.staylistaadmin.utils.ValidateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateDiscountViewModel(private val repo: DiscountRepoInterface) : ViewModel() {
    private val _errorStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow
    private val _discountStateFlow =
        MutableStateFlow<DiscountDetailsAPIState>(DiscountDetailsAPIState.Loading)
    val discountStateFlow: StateFlow<DiscountDetailsAPIState> = _discountStateFlow
    private val _validationStateFlow =
        MutableStateFlow<ValidateState>(ValidateState.BeforeValidation)
    val validationStateFlow: StateFlow<ValidateState> = _validationStateFlow

    fun validateCode(code: String) {
        if (code.isEmpty()) {
            _validationStateFlow.value =
                ValidateState.OnValidateError(R.string.discount_code_empty, Const.DISCOUNT_CODE)
        } else {
            _validationStateFlow.value = ValidateState.OnValidateSuccess(R.string.success)
        }
    }

    fun createDiscount(priceRuleId: Long , discountDetailsRoot: DiscountDetailsRoot) {
        viewModelScope.launch {
            val flow = repo.createDiscount(priceRuleId , discountDetailsRoot)
            flow.catch {
                _discountStateFlow.value = DiscountDetailsAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()?.discount_code
                    _discountStateFlow.value = DiscountDetailsAPIState.OnSuccess(data!!)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }
}