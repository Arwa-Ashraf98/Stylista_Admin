package com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.ui

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
import com.mad43.staylistaadmin.databinding.FragmentDiscountDetailsBinding
import com.mad43.staylistaadmin.discount.data.entity.DiscountCode
import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.remoteSource.DiscountRemoteSource
import com.mad43.staylistaadmin.discount.data.repo.DiscountRepo
import com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.viewModel.DiscountDetailsViewModel
import com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.viewModel.DiscountDetailsViewModelFactory
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch


class DiscountDetailsFragment : Fragment() {
    private var _binding: FragmentDiscountDetailsBinding? = null
    private val binding get() = _binding!!
    private var priceRuleId: Long = 0L
    private var discountId: Long = 0L
    private lateinit var discountDetailsVM: DiscountDetailsViewModel
    private lateinit var viewModelFactory: DiscountDetailsViewModelFactory
    private var flag = false
    private lateinit var discountCode: DiscountCode
    private lateinit var discountDetailsRoot: DiscountDetailsRoot
    private lateinit var priceRule: PriceRule


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDiscountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceRuleId = DiscountDetailsFragmentArgs.fromBundle(requireArguments()).priceRuleId
        discountId = DiscountDetailsFragmentArgs.fromBundle(requireArguments()).discountId
        priceRule = DiscountDetailsFragmentArgs.fromBundle(requireArguments()).priceRule
        initViewModel()

        discountDetailsVM.getDiscountById(priceRuleId = priceRuleId, discountId = discountId)
        observeData()
        onClicks()
    }

    private fun initViewModel() {
        val remoteSource = DiscountRemoteSource()
        val repo = DiscountRepo(remoteSource)
        viewModelFactory =
            DiscountDetailsViewModelFactory(repo)
        discountDetailsVM =
            ViewModelProvider(this, viewModelFactory)[DiscountDetailsViewModel::class.java]
    }

    private fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                discountDetailsVM.discountDetailsStateFlow.collect {
                    when (it) {
                        is DiscountDetailsAPIState.Loading -> {
                            binding.progressBarCouponsDetails.showProgress()
                        }

                        is DiscountDetailsAPIState.OnSuccess -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            val data = it.discount
                            this@DiscountDetailsFragment.discountCode = data.copy()
                            setData(discount = data)
                        }

                        is DiscountDetailsAPIState.OnFail -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            showToast(it.errorMessage.message)
                        }
                    }
                }
            }
        }
    }

    private fun setData(discount: DiscountCode) {
        binding.apply {
            editTextCouponCode.setText(discount.code)
            editTextDate.setText(
                Helpers.transformDate(
                    discount.created_at,
                    Helpers.wholeDatePattern
                )
            )
            val limit = if (priceRule.usage_limit.toString().equals("null", true)) {
                0
            } else {
                priceRule.usage_limit
            }
            editTextLimits.setText(limit.toString())
            editTextValue.setText(priceRule.value)
        }
    }

    private fun onClicks() {
        binding.apply {
            btnEditDiscount.setOnClickListener {
                flag = !flag
                if (flag) {
                    btnEditDiscount.setImageResource(R.drawable.baseline_done_all_24)
                    editTextCouponCode.enableEditText()
                    editTextLimits.enableEditText()
                    editTextValue.enableEditText()
                } else {
                    btnEditDiscount.setImageResource(R.drawable.baseline_edit_24)
                    getData()
                    observeUpdatePriceRule()
                }
            }

            imageViewBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }
        }
    }

    private fun getData() {
        val code = binding.editTextCouponCode.text.toString().trim()
        val value = binding.editTextValue.text.toString().trim()
        val limit = binding.editTextLimits.text.toString().trim()
        discountCode = DiscountCode(code = code)
        priceRule.usage_limit = limit.toInt()
        priceRule.value = value
        updatePriceRule()
    }

    private fun updateCode() {
        discountDetailsRoot = DiscountDetailsRoot(discount_code = discountCode)
        discountDetailsVM.updateDiscount(
            priceRuleId = priceRuleId,
            discountId = discountId,
            discountDetailsRoot = discountDetailsRoot
        )
    }

    private fun updatePriceRule() {
        val priceRuleRoot = PriceRuleRoot(priceRule)
        discountDetailsVM.updatePriceRule(priceRuleId, priceRuleRoot)
    }

    private fun observeUpdatePriceRule() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                discountDetailsVM.priceRuleStateFlow.collect {
                    when (it) {
                        is PriceRuleCreationAPIState.Loading -> {
                            binding.progressBarCouponsDetails.showProgress()
                        }

                        is PriceRuleCreationAPIState.OnSuccess -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            updateCode()
                            observeUpdate()
                        }

                        is PriceRuleCreationAPIState.OnFail -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            showToast(it.errorMessage.localizedMessage)
                        }
                    }
                }
            }
        }
    }

    private fun observeUpdate() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                discountDetailsVM.discountUpdateStateFlow.collect {
                    when (it) {
                        is DiscountDetailsAPIState.Loading -> {
                            binding.progressBarCouponsDetails.showProgress()
                        }

                        is DiscountDetailsAPIState.OnSuccess -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            binding.editTextCouponCode.unEnableEditText()
                            Navigation.findNavController(requireView()).navigateUp()
                            showToast(requireActivity().getString(R.string.success))
                        }

                        is DiscountDetailsAPIState.OnFail -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            showToast(it.errorMessage.message)
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