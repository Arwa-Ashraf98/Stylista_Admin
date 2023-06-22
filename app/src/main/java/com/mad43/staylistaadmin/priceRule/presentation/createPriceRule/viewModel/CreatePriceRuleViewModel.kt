package com.mad43.staylistaadmin.priceRule.presentation.createPriceRule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import com.mad43.staylistaadmin.priceRule.domain.repo.PriceRuleRepoInterface
import com.mad43.staylistaadmin.utils.Const
import com.mad43.staylistaadmin.utils.PriceRuleCreationAPIState
import com.mad43.staylistaadmin.utils.ValidateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreatePriceRuleViewModel(private val repo: PriceRuleRepoInterface) : ViewModel() {
    private val _priceRuleStateFlow =
        MutableStateFlow<PriceRuleCreationAPIState>(PriceRuleCreationAPIState.Loading)
    val priceRuleStateFlow: StateFlow<PriceRuleCreationAPIState> = _priceRuleStateFlow
    private val _errorStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow
    private val validationMutableStateFlow =
        MutableStateFlow<ValidateState>(ValidateState.BeforeValidation)
    val validationStateFlow: StateFlow<ValidateState> = validationMutableStateFlow


    fun validateData(
        title: String,
        value: String,
        valueType: String,
        startData: String,
        endDate: String,
        limit: String
    ) {
        if (title.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.title_must_not_be_empty,
                    Const.TITLE
                )

        } else if (valueType.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.value_type_must_not_be_empty,
                    Const.VALUE_TYPE
                )
        } else if (value.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.value_must_not_be_empty,
                    Const.VALUE
                )
        } else if (startData.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.start_date_must_not_be_empty,
                    Const.START_DATE
                )
        } else if (endDate.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.end_date_must_not_be_empty,
                    Const.END_DATE
                )
        } else if (limit.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.limit_name_must_not_be_empty,
                    Const.LIMIT
                )
        } else {
            validationMutableStateFlow.value =
                ValidateState.OnValidateSuccess(R.string.success)
        }
    }

    fun createPriceRule(priceRuleRoot: PriceRuleRoot) {
        viewModelScope.launch {
            val flow = repo.createPriceRule(priceRuleRoot)
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
}