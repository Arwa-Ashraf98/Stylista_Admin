package com.mad43.staylistaadmin.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.login.data.entity.LoginModel
import com.mad43.staylistaadmin.login.domain.repo.LoginRepoInterface
import com.mad43.staylistaadmin.utils.Const
import com.mad43.staylistaadmin.utils.ValidateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

class LoginViewModel(private val repoInterface: LoginRepoInterface) : ViewModel() {

    private var _loginState: MutableStateFlow<ValidateState> =
        MutableStateFlow(ValidateState.BeforeValidation)
    var loginState: StateFlow<ValidateState> = _loginState


    fun validateLogin(loginModel: LoginModel) {
        if (loginModel.shopName.isEmpty()) {
            _loginState.value = ValidateState.OnValidateError(R.string.empty_shop_name, "login")
        } else if (loginModel.password.isEmpty()) {
            _loginState.value = ValidateState.OnValidateError(R.string.empty_password, "login")
        } else {
            validateData(loginModel)
        }
    }

    private fun validateData(loginModel: LoginModel){
        if (loginModel.shopName.trim() != Const.SHOP_NAME_VALUE.trim()){
            _loginState.value = ValidateState.OnValidateError(R.string.shop_name_wrong, "login")
        }else if (loginModel.password.trim() != Const.SHOP_PASSWORD_VALUE.trim()){
            _loginState.value = ValidateState.OnValidateError(R.string.wrong_password , "login")
        }else {
            _loginState.value= ValidateState.OnValidateSuccess(R.string.success)
        }
    }

    fun saveShopData(shopName: String, password: String) {
        repoInterface.saveShopName(shopName)
        repoInterface.savePassword(password)
    }

    fun getShopName() {
        repoInterface.getShopName()
    }

    fun getPassword() {
        repoInterface.getPassword()
    }


}