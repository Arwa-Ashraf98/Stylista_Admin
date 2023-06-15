package com.mad43.staylistaadmin.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.staylistaadmin.login.domain.repo.LoginRepoInterface

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory (private val repoInterface: LoginRepoInterface) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            LoginViewModel(repoInterface) as T
        }else {
            throw IllegalArgumentException("Login Product View model class cannot be found")
        }
    }
}