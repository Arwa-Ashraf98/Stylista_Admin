package com.mad43.staylistaadmin.priceRule.presentation.ui

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
import androidx.navigation.fragment.findNavController
import com.mad43.staylistaadmin.databinding.FragmentPriceRuleBinding
import com.mad43.staylistaadmin.discount.presentation.ui.DiscountFragmentArgs
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.priceRule.data.remote.PriceRuleRemoteSource
import com.mad43.staylistaadmin.priceRule.data.repo.PriceRuleRepo
import com.mad43.staylistaadmin.priceRule.presentation.viewModel.PriceRuleViewModel
import com.mad43.staylistaadmin.priceRule.presentation.viewModel.PriceRuleViewModelFactory
import com.mad43.staylistaadmin.product.presentation.getAllProduct.ui.ProductFragmentDirections
import com.mad43.staylistaadmin.utils.PriceRuleAPIState
import com.mad43.staylistaadmin.utils.hideProgress
import com.mad43.staylistaadmin.utils.showProgress
import com.mad43.staylistaadmin.utils.showToast
import kotlinx.coroutines.launch


class PriceRuleFragment : Fragment() {
    private var _binding: FragmentPriceRuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PriceRuleAdapter
    private lateinit var priceRuleViewModel: PriceRuleViewModel
    private lateinit var priceRuleViewModelFactory: PriceRuleViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        priceRuleViewModel.getAllPriceRule()
        observeData()
        adapter = PriceRuleAdapter()
        onClicks()

    }

    private fun initViewModel() {
        val remoteSource = PriceRuleRemoteSource()
        val repo = PriceRuleRepo(remoteSource)
        priceRuleViewModelFactory =
            PriceRuleViewModelFactory(repo)
        priceRuleViewModel =
            ViewModelProvider(this, priceRuleViewModelFactory)[PriceRuleViewModel::class.java]
    }

    private fun onClicks() {
        adapter.setOnPriceRuleClickListener(object : PriceRuleAdapter.OnPriceRuleListener {
            override fun onPriceRuleClickListener(id: Long) {
                val action =
                    PriceRuleFragmentDirections.actionPriceRuleFragmentToDiscountFragment(id)
                findNavController().navigate(action)
            }
        })

        binding.apply {
            imageViewBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }

            btnCreatePriceRule.setOnClickListener {

            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                priceRuleViewModel.priceRuleStateFlow.collect {
                    when (it) {
                        is PriceRuleAPIState.Loading -> {
                            binding.progressBarPriceRule.showProgress()
                        }
                        is PriceRuleAPIState.OnSuccess -> {
                            binding.progressBarPriceRule.hideProgress()
                            val data = it.priceRule.price_rules
                            initRecycler(data)
                        }

                        is PriceRuleAPIState.OnFail -> {
                            binding.progressBarPriceRule.hideProgress()
                            showToast(it.errorMessage.localizedMessage)
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler(list: List<PriceRule>) {
        adapter.setList(list)
        binding.recyclerPriceRule.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}