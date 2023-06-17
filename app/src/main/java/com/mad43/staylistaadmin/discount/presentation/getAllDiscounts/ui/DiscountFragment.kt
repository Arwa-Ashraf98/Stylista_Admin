package com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentDiscountBinding
import com.mad43.staylistaadmin.discount.data.entity.DiscountCode
import com.mad43.staylistaadmin.discount.data.remoteSource.DiscountRemoteSource
import com.mad43.staylistaadmin.discount.data.repo.DiscountRepo
import com.mad43.staylistaadmin.discount.presentation.createDiscount.ui.CreateDiscountFragment
import com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel.DiscountViewModel
import com.mad43.staylistaadmin.discount.presentation.getAllDiscounts.viewModel.DiscountViewModelFactory
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch


class DiscountFragment : Fragment() {
    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DiscountAdapter
    private lateinit var discountViewModel: DiscountViewModel
    private lateinit var viewModelFactory: DiscountViewModelFactory
    private var id = 0L
    private lateinit var priceRule: PriceRule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "onCreate: ", )
    }
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
        Log.e("TAG", "onViewCreated: ", )
        id = DiscountFragmentArgs.fromBundle(requireArguments()).id
        priceRule = DiscountFragmentArgs.fromBundle(requireArguments()).priceRule
        initViewModel()
        discountViewModel.getAllDiscount(id)
        observeData()
        adapter = DiscountAdapter()
        onClicks()
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResume: ", )
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: ", )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: ", )
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "onStop: ", )
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
                val dialog = CreateDiscountFragment()
                val args = Bundle()
                args.putLong(Const.PRICE_RULE_ID, id)
                dialog.arguments = args
                dialog.show(requireActivity().supportFragmentManager , "dialog")
            }

            imageViewBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }

            adapter.setOnDiscountClickListener(object : DiscountAdapter.DiscountClickListener {
                override fun onDiscountClickListener(priceRuleId: Long, discountId: Long) {
                    val limits = if (priceRule.usage_limit == null) {
                        0
                    } else {
                        priceRule.usage_limit
                    }
                    val action =
                        DiscountFragmentDirections.actionDiscountFragmentToDiscountDetailsFragment(
                            discountId,
                            priceRuleId,
                            priceRule.value as String,
                            limits as Int
                        )
                    findNavController().navigate(action)
                }

                override fun onDeleteClickListener(discountId: Long, priceRuleId: Long) {
                    showDialog(priceId = priceRuleId, discountId)
                }


            })
        }
    }

    private fun showDialog(priceId: Long, discountId: Long) {
        Dialogs.showConfirmationDialog(
            requireActivity().applicationContext, getString(R.string.delete_discount), {
                this@DiscountFragment.discountViewModel.deleteDiscount(
                    priceRuleId = priceId,
                    discountId = discountId
                )
            }, {
                showToast(requireActivity().getString(R.string.deleted_cancelled))
            }
        )
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "onPause: ", )
    }

    private fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                discountViewModel.discountStateFlow.collect {
                    when (it) {
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

    private fun initRecycler(list: List<DiscountCode>) {
        adapter.setList(list)
        binding.recyclerViewCoupons.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("TAG", "onDestroyView: ", )
        _binding = null
    }

}