package com.mad43.staylistaadmin.product.presentation.productDetails.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.product.data.entity.InventoryLevel
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface
import com.mad43.staylistaadmin.utils.InventoryLevelAPIState
import com.mad43.staylistaadmin.utils.ProductAPIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val repo: RepoInterface) : ViewModel() {
    private val dataMutableStateFlow = MutableStateFlow<ProductAPIState>(ProductAPIState.Loading)
    val dataStateFlow: StateFlow<ProductAPIState> = dataMutableStateFlow
    private val _inventoryStateFlow =
        MutableStateFlow<InventoryLevelAPIState>(InventoryLevelAPIState.Loading)
    val inventoryStateFlow: StateFlow<InventoryLevelAPIState> = _inventoryStateFlow
    private val _errorStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = _errorStateFlow


    fun getProductById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val flow = repo.getProductById(id)
            flow.catch {
                dataMutableStateFlow.value = ProductAPIState.OnFail(it)
            }.collect { response ->
                Log.e("TAG1", response.body().toString())
                if (response.isSuccessful) {
                    val data = response.body()
                    dataMutableStateFlow.value = ProductAPIState.OnSuccess(data!!)
                } else {
                    _errorStateFlow.value = response.message()
                }
            }
        }
    }

    fun updateQuantity(inventoryLevel: InventoryLevel) {
        viewModelScope.launch {
            val flow = repo.updateQuantity(inventoryLevel)
            flow.catch {
                _inventoryStateFlow.value = InventoryLevelAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    val data = it.body()
                    _inventoryStateFlow.value = InventoryLevelAPIState.OnSuccess(data!!)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }

    fun updateProductById(id: Long, secondProductModel: SecondProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val flow = repo.updateProduct(id = id, secondProductModel)
            flow.catch {
                dataMutableStateFlow.value = ProductAPIState.OnFail(it)
            }.collect {
                if (it.isSuccessful) {
                    dataMutableStateFlow.value =
                        ProductAPIState.OnSuccess(it.body() as SecondProductModel)
//                    getProductById(it.body()?.product?.id as Long)
                } else {
                    _errorStateFlow.value = it.message()
                }
            }
        }
    }


}