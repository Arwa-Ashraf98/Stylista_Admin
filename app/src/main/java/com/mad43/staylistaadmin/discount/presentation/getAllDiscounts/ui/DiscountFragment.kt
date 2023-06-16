package com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentDiscountBinding
import com.mad43.staylistaadmin.discount.data.entity.DiscountCode
import com.mad43.staylistaadmin.discount.data.remoteSource.DiscountRemoteSource
import com.mad43.staylistaadmin.discount.data.repo.DiscountRepo
import com.mad43.staylistaadmin.discount.presentation.ui.DiscountFragmentArgs
import com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel.DiscountViewModel
import com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel.DiscountViewModelFactory
import com.mad43.staylistaadmin.priceRule.data.remote.PriceRuleRemoteSource
import com.mad43.staylistaadmin.priceRule.data.repo.PriceRuleRepo
import com.mad43.staylistaadmin.priceRule.presentation.viewModel.PriceRuleViewModel
import com.mad43.staylistaadmin.priceRule.presentation.viewModel.PriceRuleViewModelFactory
import com.mad43.staylistaadmin.utils.DiscountAPIState
import com.mad43.staylistaadmin.utils.hideProgress
import com.mad43.staylistaadmin.utils.showProgress
import com.mad43.staylistaadmin.utils.showToast
import kotlinx.coroutines.launch


class DiscountFragment : Fragment() {
    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DiscountAdapter
    private lateinit var discountViewModel: DiscountViewModel
    private lateinit var viewModelFactory: DiscountViewModelFactory
    private var id = 0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = DiscountFragmentArgs.fromBundle(requireArguments()).id
        initViewModel()
        discountViewModel.getAllDiscount(id)
        observeData()
        adapter = DiscountAdapter()
        onClicks()
    }

    private fun initViewModel() {
        val remoteSource = DiscountRemoteSource()
        val repo = DiscountRepo(remoteSource)
        viewModelFactory =
            DiscountViewModelFactory(repo)
        discountViewModel =
            ViewModelProvider(this, viewModelFactory)[DiscountViewModel::class.java]
    }


    private fun onClicks() {
        binding.apply {
            btnCreateCoupons.setOnClickListener {

            }

            imageViewBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }
        }
    }

    private fun observeData(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                discountViewModel.discountStateFlow.collect{
                    when(it){
                        is DiscountAPIState.Loading -> {
                            binding.progressBarCoupons.showProgress()
                        }

                        is DiscountAPIState.OnSuccess -> {
                            binding.progressBarCoupons.hideProgress()
                            val data = it.discount.discount_codes
                            initRecycler(data)

                        }

                        is DiscountAPIState.OnFail -> {
                            binding.progressBarCoupons.hideProgress()
                            showToast(it.errorMessage.message)

                        }
                    }
                }
            }
        }
    }

    private fun initRecycler(list : List<DiscountCode>){
        adapter.setList(list)
        binding.recyclerViewCoupons.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}