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
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentDiscountDetailsBinding
import com.mad43.staylistaadmin.discount.data.entity.DiscountCode
import com.mad43.staylistaadmin.discount.data.entity.DiscountDetailsRoot
import com.mad43.staylistaadmin.discount.data.remoteSource.DiscountRemoteSource
import com.mad43.staylistaadmin.discount.data.repo.DiscountRepo
import com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.viewModel.DiscountDetailsViewModel
import com.mad43.staylistaadmin.discount.presentation.getDiscountDetails.viewModel.DiscountDetailsViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch


class DiscountDetailsFragment : Fragment() {
    private var _binding: FragmentDiscountDetailsBinding? = null
    private val binding get() = _binding!!
    private var priceRuleId: Long = 0L
    private var discountId: Long = 0L
    private var value: String = ""
    private var limits: Int = 0
    private lateinit var discountDetailsVM: DiscountDetailsViewModel
    private lateinit var viewModelFactory: DiscountDetailsViewModelFactory
    private var flag = false
    private lateinit var discountCode: DiscountCode
    private lateinit var discountDetailsRoot: DiscountDetailsRoot

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
        limits = DiscountDetailsFragmentArgs.fromBundle(requireArguments()).limits
        value = DiscountDetailsFragmentArgs.fromBundle(requireArguments()).value
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
            editTextDate.setText(discount.created_at)
            editTextLimits.setText(limits.toString())
            editTextValue.setText(value)
        }
    }

    private fun onClicks() {
        binding.apply {
            btnEditDiscount.setOnClickListener {
                flag = !flag
                if (flag) {
                    btnEditDiscount.setImageResource(R.drawable.baseline_save_alt_24)
                    editTextCouponCode.enableEditText()
                } else {
                    btnEditDiscount.setImageResource(R.drawable.baseline_edit_24)
                    getData()
                    observeUpdate()

                }
            }
        }
    }

    private fun getData() {
        val code = binding.editTextCouponCode.text.toString().trim()
        discountCode = DiscountCode(code = code)
        updateCode(discountCode)
    }

    private fun updateCode(discount: DiscountCode) {
        discountDetailsRoot = DiscountDetailsRoot(discount_code = discountCode)
        discountDetailsVM.updateDiscount(
            priceRuleId = priceRuleId,
            discountId = discountId,
            discountDetailsRoot = discountDetailsRoot
        )
    }

    private fun observeUpdate() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                discountDetailsVM.discountUpdateStateFlow.collect {
                    when (it) {
                        is DiscountDetailsAPIState.Loading -> {
                            binding.progressBarCouponsDetails.showProgress()
                        }

                        is DiscountDetailsAPIState.OnSuccess -> {
                            binding.progressBarCouponsDetails.hideProgress()
                            binding.editTextCouponCode.unEnableEditText()
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