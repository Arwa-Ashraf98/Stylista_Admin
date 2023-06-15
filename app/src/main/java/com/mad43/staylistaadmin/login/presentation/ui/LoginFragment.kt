package com.mad43.staylistaadmin.login.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentLoginBinding
import com.mad43.staylistaadmin.login.data.entity.LoginModel
import com.mad43.staylistaadmin.login.data.local.LocalSource
import com.mad43.staylistaadmin.login.data.repo.LoginRepo
import com.mad43.staylistaadmin.login.presentation.viewModel.LoginViewModel
import com.mad43.staylistaadmin.login.presentation.viewModel.LoginViewModelFactory
import com.mad43.staylistaadmin.product.data.remote.RemoteSource
import com.mad43.staylistaadmin.product.data.repo.Repo
import com.mad43.staylistaadmin.product.presentation.productDetails.viewModel.ProductDetailsViewModel
import com.mad43.staylistaadmin.product.presentation.productDetails.viewModel.ProductDetailsViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch
import kotlin.math.log


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginVMFactory: LoginViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        onClicks()


    }

    private fun initViewModel() {
        val localSource = LocalSource()
        val repo = LoginRepo(localSource)
        loginVMFactory =
            LoginViewModelFactory(repo)
        loginViewModel =
            ViewModelProvider(this, loginVMFactory)[LoginViewModel::class.java]
    }

    private fun onClicks() {
        binding.apply {
            btnLogin.setOnClickListener {
                getData()
            }
        }
    }

    private fun getData() {
        val shopName = binding.emailEdiText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val loginModel = LoginModel(shopName, password)
        loginViewModel.validateLogin(loginModel)
        observeValidation(shopName , password)
    }

    private fun observeValidation(password : String , shopName : String) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginState.collect {
                    when (it) {
                        is ValidateState.OnValidateSuccess -> {
                            binding.progressBarLogin.hideProgress()
                            this@LoginFragment.loginViewModel.saveShopData(shopName, password)
                            navigateToNextScreen(R.id.action_loginFragment_to_homeFragment2)
                        }

                        is ValidateState.OnValidateError -> {
                            binding.progressBarLogin.hideProgress()
                            showToast(requireActivity().getString(it.message))
                        }

                        is ValidateState.BeforeValidation -> {
                            binding.progressBarLogin.showProgress()
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}