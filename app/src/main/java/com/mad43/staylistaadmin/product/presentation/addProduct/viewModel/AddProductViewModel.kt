package com.mad43.staylistaadmin.product.presentation.addProduct.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.product.data.entity.ImageRoot
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel
import com.mad43.staylistaadmin.product.domain.repo.RepoInterface
import com.mad43.staylistaadmin.utils.Const
import com.mad43.staylistaadmin.utils.ProductAPIState
import com.mad43.staylistaadmin.utils.ValidateState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddProductViewModel(private val repo: RepoInterface) : ViewModel() {

    private val dataMutableStateFlow = MutableStateFlow<ProductAPIState>(ProductAPIState.Loading)
    val dataStateFlow: StateFlow<ProductAPIState> = dataMutableStateFlow
    private val errorMStateFlow = MutableStateFlow("")
    val errorStateFlow: StateFlow<String> = errorMStateFlow
    private val validationMutableStateFlow =
        MutableStateFlow<ValidateState>(ValidateState.BeforeValidation)
    val validationStateFlow: StateFlow<ValidateState> = validationMutableStateFlow
    private val _type = MutableStateFlow<String>("")
    val type: StateFlow<String> = _type


    fun setType(type: String) {
        _type.value = type
    }


    fun validateVariantData(price: String, quantity: String?, color: String, size: String) {
        if (price.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.price_must_not_be_empty, Const.PRICE)
        } else if (quantity.isNullOrEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.quantity_must_not_be_empty, Const.QUANTITY)
        } else if (color.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.color_must_not_be_empty, Const.COLOR)
        } else if (size.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.size_must_not_be_empty, Const.SIZE)
        } else {
            validationMutableStateFlow.value = ValidateState.OnValidateSuccess(R.string.success)
        }
    }

    fun validateImageUrl(url: String) {
        if (url.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.url_must_not_be_empty, Const.IMAGE_URL)
        } else {
            validationMutableStateFlow.value = ValidateState.OnValidateSuccess(R.string.success)
        }
    }

    fun validateProduct(
        title: String,
        tags: String,
        description: String,
        poster: String,
        vendor: String,
        type: String
    ) {
        if (poster.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.poster_must_not_be_empty, Const.POSTER)

        } else if (title.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.product_name_must_not_be_empty,
                    Const.PRODUCT_NAME
                )
        } else if (type.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.product_type_must_not_be_empty,
                    Const.PRODUCT_TYPE
                )

        } else if (vendor.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.poster_must_not_be_empty, Const.VENDOR)

        } else if (description.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(
                    R.string.description_must_not_be_empty,
                    Const.DESCRIPTION
                )
        } else if (tags.isEmpty()) {
            validationMutableStateFlow.value =
                ValidateState.OnValidateError(R.string.tags_must_not_be_empty, Const.TAGS)
        } else {
            validationMutableStateFlow.value = ValidateState.OnValidateSuccess(R.string.success)
        }

    }

    fun addProduct(productModel: SecondProductModel, imageRoot: ImageRoot) {
        viewModelScope.launch(Dispatchers.IO) {
            val flow = repo.createProduct(productModel)
            flow.catch {
                dataMutableStateFlow.value = ProductAPIState.OnFail(it)
            }.collect { response ->
                if (response.isSuccessful) {
                    val data = response.body()
                    uploadPosterImage(data?.product?.id as Long, imageRoot)
                    dataMutableStateFlow.value = ProductAPIState.OnSuccess(data)
                } else {
                    errorMStateFlow.value = response.message()
                }
            }
        }
    }

    suspend fun updateProduct(id: Long, secondProductModel: SecondProductModel) {
        val flow = repo.updateProduct(id, secondProductModel)
        flow.catch {
            dataMutableStateFlow.value = ProductAPIState.OnFail(it)
        }.collect {
            val data = it.body()
            if (it.isSuccessful) {
                dataMutableStateFlow.value = ProductAPIState.OnSuccess(data!!)
//                validationMutableStateFlow.value = ValidateState.OnValidateSuccess(R.string.success)
            } else {

//                validationMutableStateFlow.value =
//                    ValidateState.OnValidateError(R.string.failed_update_data, Const.UPDATE)
            }
        }
    }

    private suspend fun uploadPosterImage(id: Long, imageRoot: ImageRoot) {
        repo.uploadPosterImage(id, imageRoot)
    }
}