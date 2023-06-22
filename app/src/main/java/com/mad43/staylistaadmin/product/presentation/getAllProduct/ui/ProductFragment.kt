package com.mad43.staylistaadmin.product.presentation.getAllProduct.ui

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
import com.mad43.staylistaadmin.databinding.FragmentProductBinding
import com.mad43.staylistaadmin.product.data.entity.Product
import com.mad43.staylistaadmin.product.data.remote.RemoteSource
import com.mad43.staylistaadmin.product.data.repo.Repo
import com.mad43.staylistaadmin.product.presentation.getAllProduct.viewModel.ProductViewModel
import com.mad43.staylistaadmin.product.presentation.getAllProduct.viewModel.ProductViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch


class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productViewModelFactory: ProductViewModelFactory
    private lateinit var adapter: ProductAdapter
    private lateinit var networkChecker: NetworkChecker
    private var id: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TAG", "onViewCreated: ")
        id = 0L
        networkChecker = NetworkChecker()
        adapter = ProductAdapter()
        initViewModel()
        getAllProduct()
        getData()
        swipeToRefresh()
        onClicks()


    }

    private fun initViewModel() {
        productViewModelFactory =
            ProductViewModelFactory(Repo.getRepo(RemoteSource.getRemoteSource()), networkChecker)
        productViewModel = ViewModelProvider(
            this,
            productViewModelFactory
        )[ProductViewModel::class.java]
    }

    private fun getAllProduct() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.getAllProduct()

            }
        }
    }

    private fun swipeToRefresh() {
        binding.swipeHome.setOnRefreshListener {
            getAllProduct()
            getData()
            binding.swipeHome.isRefreshing = false
        }
    }

    private fun getData() {
        getErrorMessage()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.dataStateFlow.collect {
                    when (it) {
                        is APIState.OnSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            val list = it.productModel.products
                            setRecycler(list)
                        }

                        is APIState.OnFail -> {
                            binding.progressBar.visibility = View.GONE
                            showToast("check ${it.errorMessage.message}")
                        }

                        is APIState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun getErrorMessage() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.errorStateFlow.collect {
                    showToast(it)
                }
            }
        }
    }

    private fun onClicks() {
        binding.apply {
            imageViewBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }
        }
        adapter.setOnItemClickListener(object : ProductAdapter.SetOnItemDetailsClickListener {
            override fun onItemClickListener(id: Long) {
                val action =
                    ProductFragmentDirections.actionProductFragmentToProductDetailsFragment2(id)
                findNavController().navigate(action)
            }

            override fun onDeleteClickListener(productId: Long) {
                this@ProductFragment.id = productId
                showDialog()
            }
        })

        binding.btnAddProduct.setOnClickListener {
            navigateToNextScreen(R.id.action_productFragment_to_addProductFragment)
        }
    }

    private fun showDialog() {
        Dialogs.showConfirmationDialog(requireContext(),
            getString(R.string.sure_of_delete_product),
            {
                deleteProduct()
            },
            {
                showToast(getString(R.string.deleted_cancelled))
            })
    }

    private fun deleteProduct() {
        productViewModel.deleteProduct(id as Long)
//        observeDeleteProcess()
    }

    private fun observeDeleteProcess() {
        getErrorMessageFromDeleteProcess()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.deletedDataStateFlow.collect {
                    if (!it) {
                        showToast(getString(R.string.deleted_successfully))
                    } else {
                        showToast(getString(R.string.somthing_wrong_with_delete))
                    }
                }
            }
        }
    }

    private fun getErrorMessageFromDeleteProcess() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.deleteErrorStateFlow.collect {
                    showToast(it)
                }
            }
        }
    }

    private fun setRecycler(list: List<Product>) {
        adapter.setList(list)
        binding.recyclerProducts.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("TAG", "onDestroyView: ")
        _binding = null
    }

}