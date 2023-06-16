package com.mad43.staylistaadmin.product.presentation.getAllProduct.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.product.data.entity.ProductModel
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface
import com.mad43.staylistaadmin.utils.APIState
import com.mad43.staylistaadmin.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repo: RepoInterface,
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val mutableStateFlow = MutableStateFlow<APIState>(APIState.Loading)
    val dataStateFlow: StateFlow<APIState> = mutableStateFlow
    private val errorMStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = errorMStateFlow
    private val deleteErrorMStateFlow = MutableStateFlow("")
    val deleteErrorStateFlow: StateFlow<String> = errorMStateFlow
    private val deletedMutableStateFlow = MutableStateFlow(false)
    val deletedDataStateFlow: StateFlow<Boolean> = deletedMutableStateFlow

    init {
        Log.e("TAG", ": ", )
        viewModelScope.launch {
            getAllProduct()
        }
    }

    suspend fun getAllProduct() {
        val flow = repo.getAllProduct()
        flow.catch {
            mutableStateFlow.value = APIState.OnFail(it)
        }.collect { response ->
            if (response.isSuccessful) {
                val data = response.body()
                mutableStateFlow.value = APIState.OnSuccess(data as ProductModel)
            } else {
                errorMStateFlow.value = response.errorBody().toString()
            }
        }

    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteProduct(id)

            getAllProduct()

            if (networkChecker.isOnline()) {
                Log.e("TAG", "true")

//                val flow = repo.deleteProduct(id)
//                flow.catch {
//                    deleteErrorMStateFlow.value = it.message as String
//                }.collect {
//                    deletedMutableStateFlow.value = true
//                }
            } else {
                Log.e("TAG", "false")
//                "Check network first".also { deleteErrorMStateFlow.value = it }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "onCleared: ")
    }

}


