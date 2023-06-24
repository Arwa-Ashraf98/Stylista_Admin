package com.mad43.staylistaadmin.discount.presentation.createDiscount.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentCreateDiscountBinding
import com.mad43.staylistaadmin.discount.data.entity.DiscountCode
import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.remoteSource.DiscountRemoteSource
import com.mad43.staylistaadmin.discount.data.repo.DiscountRepo
import com.mad43.staylistaadmin.discount.presentation.createDiscount.viewModel.CreateDiscountViewModel
import com.mad43.staylistaadmin.discount.presentation.createDiscount.viewModel.CreateDiscountViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch


class CreateDiscountFragment : Fragment() {

    private var _binding: FragmentCreateDiscountBinding? = null
    private val binding get() = _binding!!
    private lateinit var createDiscountViewModel: CreateDiscountViewModel
    private lateinit var viewModelFactory: CreateDiscountViewModelFactory
    private var priceRuleId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceRuleId = CreateDiscountFragmentArgs.fromBundle(requireArguments()).priceRuleId
        initViewModel()
        onClicks()

    }

    private fun initViewModel() {
        val remoteSource = DiscountRemoteSource()
        val repo = DiscountRepo(remoteSource)
        viewModelFactory =
            CreateDiscountViewModelFactory(repo)
        createDiscountViewModel =
            ViewModelProvider(this, viewModelFactory)[CreateDiscountViewModel::class.java]
    }

    private fun onClicks() {
        binding.apply {
            btnAddDiscount.setOnClickListener {
                getData()
            }

            imageViewBackDiscounts.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }
        }
    }

    private fun getData() {
        val discountCode = binding.editTextCodeDiscount.text.toString().trim()
        createDiscountViewModel.validateCode(discountCode)
        observeValidateData(discountCode)
    }

    private fun observeValidateData(code: String) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createDiscountViewModel.validationStateFlow.collect {
                    when (it) {
                        is ValidateState.BeforeValidation -> {
                            binding.progressBarDiscountsCreation.showProgress()
                        }
                        is ValidateState.OnValidateSuccess -> {
                            binding.progressBarDiscountsCreation.hideProgress()
                            createDiscount(code)
                            observeDiscountCreation()
                        }

                        is ValidateState.OnValidateError -> {
                            if (it.place.equals(Const.DISCOUNT_CODE, true)) {
                                binding.progressBarDiscountsCreation.hideProgress()
                                showToast(requireActivity().getString(it.message))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createDiscount(code: String) {
        val discountCode = DiscountCode(code = code)
        val discountDetailsRoot = DiscountDetailsRoot(discountCode)
        createDiscountViewModel.createDiscount(priceRuleId, discountDetailsRoot)
    }

    private fun observeDiscountCreation() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createDiscountViewModel.discountStateFlow.collect {
                    when (it) {
                        is DiscountDetailsAPIState.Loading -> {
                            binding.progressBarDiscountsCreation.showProgress()
                        }

                        is DiscountDetailsAPIState.OnSuccess -> {
                            binding.progressBarDiscountsCreation.hideProgress()
                            val data = it.discount
                            if (data.code != null) {
                                showToast(requireActivity().getString(R.string.success))
                            }
                        }

                        is DiscountDetailsAPIState.OnFail -> {
                            binding.progressBarDiscountsCreation.hideProgress()
                            showToast(it.errorMessage.localizedMessage)
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