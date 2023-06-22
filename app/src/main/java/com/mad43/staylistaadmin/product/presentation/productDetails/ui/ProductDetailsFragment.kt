package com.mad43.staylistaadmin.product.presentation.productDetails.ui

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
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentProductDetailsBinding
import com.mad43.staylistaadmin.product.data.entity.Image
import com.mad43.staylistaadmin.product.data.entity.InventoryLevel
import com.mad43.staylistaadmin.product.data.entity.SecondProductModel
import com.mad43.staylistaadmin.product.data.entity.Variant
import com.mad43.staylistaadmin.product.data.remote.RemoteSource
import com.mad43.staylistaadmin.product.data.repo.Repo
import com.mad43.staylistaadmin.product.presentation.productDetails.viewModel.ProductDetailsViewModel
import com.mad43.staylistaadmin.product.presentation.productDetails.viewModel.ProductDetailsViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch


class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailedProductViewModel: ProductDetailsViewModel
    private lateinit var detailedProductVMFactory: ProductDetailsViewModelFactory
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var secondProductModel: SecondProductModel
    private var id: Long? = null
    private var flag: Boolean = false
    private var variantFlag = false
    private lateinit var inventoryLevel: InventoryLevel
    private var quantity: String = ""
    private var inventoryId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = ProductDetailsFragmentArgs.fromBundle(requireArguments()).id
        initViewModel()
        detailedProductViewModel.getProductById(id!!)
        imageAdapter = ImageAdapter()
        sizeAdapter = SizeAdapter()
        onClicks()
        getData()
        swipeToRefresh()
    }

    private fun initViewModel() {
        detailedProductVMFactory =
            ProductDetailsViewModelFactory(Repo.getRepo(RemoteSource.getRemoteSource()))
        detailedProductViewModel =
            ViewModelProvider(this, detailedProductVMFactory)[ProductDetailsViewModel::class.java]
    }

    private fun swipeToRefresh() {
        binding.swipDetails.setOnRefreshListener {
            detailedProductViewModel.getProductById(id!!)
            getData()
            binding.swipDetails.isRefreshing = false
        }
    }

    private fun getData() {
        observeError()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailedProductViewModel.dataStateFlow.collect {
                    when (it) {
                        is ProductAPIState.OnSuccess -> {
                            binding.progressBarDeatils.visibility = View.GONE
                            val data = it.product
                            this@ProductDetailsFragment.secondProductModel = data
                            setData(data)
                            initImageRecycler(data.product.images as List<Image>)
                        }

                        is ProductAPIState.OnFail -> {
                            binding.progressBarDeatils.visibility = View.GONE
                            showToast(it.errorMessage.message)
                        }

                        is ProductAPIState.Loading -> {
                            binding.progressBarDeatils.visibility = View.VISIBLE
                        }

                    }
                }
            }
        }
    }

    private fun setData(data: SecondProductModel) {
        binding.apply {
            binding.imageViewDetailedProduct.loadImage(data.product.image?.src as String)
            editTextDetailedColor.setText(data.product.options?.get(1)?.values?.get(0) ?: "Nothing")
            editTextDetailedPrice.setText(data.product.variants?.get(0)?.price)
            val values = data.product.options?.get(0)?.values
            val variantList = data.product.variants
            initSizeRecycler(values!!)
            setVariantList(variantList!!)
            editTextVendor.setText(data.product.vendor)
            editTextDetailedTitle.setText(data.product.title)
            editTextDetailedType.setText(data.product.product_type)
        }
    }

    private fun onClicks() {
        binding.apply {

            btnEdit.setOnClickListener {
                flag = !flag
                if (flag) {
                    btnSave.visibilityVisible()
                    editTextDetailedTitle.enableEditText()
                    editTextDetailedType.enableEditText()
                    editTextVendor.enableEditText()
                } else {
                    btnSave.visibilityGone()
                    editTextDetailedTitle.unEnableEditText()
                    editTextDetailedType.unEnableEditText()
                    editTextVendor.unEnableEditText()
                }
            }

            btnSave.setOnClickListener {
                val title = editTextDetailedTitle.text.toString().trim()
                val type = editTextDetailedType.text.toString().trim()
                val vendor = editTextVendor.text.toString().trim()
                secondProductModel.product.vendor = vendor
                secondProductModel.product.title = title
                secondProductModel.product.product_type = type
                updateProduct(
                    secondProductModel = secondProductModel,
                    id = secondProductModel.product.id as Long
                )
                observeUpdate()
            }

            imageViewBack.setOnClickListener {
                Navigation.findNavController(it)
                    .navigateUp()
            }

            imageViewEdit.setOnClickListener {
                variantFlag = !variantFlag
                if (variantFlag) {
                    binding.imageViewEdit.setImageResource(R.drawable.baseline_done_all_24)
                    binding.editTextDetailedQuantity.enableEditText()
                } else {
                    getQuantity()
                    binding.imageViewEdit.setImageResource(R.drawable.baseline_edit_24)
                    binding.editTextDetailedQuantity.unEnableEditText()
                }
            }
        }



        sizeAdapter.setOnItemSizeClickListener(object : SizeAdapter.OnSizeItemClickListener {
            override fun setOnSizeItemClickListener(quantity: String, inventoryItemId: Long) {
                binding.editTextDetailedQuantity.setText(quantity)
                this@ProductDetailsFragment.quantity = quantity
                this@ProductDetailsFragment.inventoryId = inventoryItemId
            }

        })
    }

    private fun getQuantity() {
        var quantity = binding.editTextDetailedQuantity.text.toString().trim()
        quantity = if (quantity.isEmpty()) {
            "0"
        } else {
            binding.editTextDetailedQuantity.text.toString().trim()
        }
        inventoryLevel = InventoryLevel(quantity.toInt(), inventoryId)
        if (quantity != this@ProductDetailsFragment.quantity && quantity != "0") {
            detailedProductViewModel.updateQuantity(inventoryLevel)
            observeUpdateQuantity()
        } else {
            showToast(requireActivity().getString(R.string.quantity_same))
        }
    }

    private fun updateProduct(id: Long, secondProductModel: SecondProductModel) {
        detailedProductViewModel.updateProductById(id, secondProductModel)
    }

    private fun observeUpdate() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailedProductViewModel.dataStateFlow.collect {
                    when (it) {
                        is ProductAPIState.OnSuccess -> {
                            binding.progressBarDeatils.hideProgress()
                            showToast(requireActivity().getString(R.string.success))
                            Navigation.findNavController(requireView()).navigateUp()
                            binding.btnSave.visibilityGone()
                        }

                        is ProductAPIState.OnFail -> {
                            binding.progressBarDeatils.hideProgress()
                            showToast(it.errorMessage.message)
                            binding.btnSave.visibilityGone()
                        }

                        is ProductAPIState.Loading -> {
                            binding.progressBarDeatils.showProgress()
                        }
                    }
                }
            }
        }
    }

    private fun observeUpdateQuantity() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailedProductViewModel.inventoryStateFlow.collect {
                    when (it) {
                        is InventoryLevelAPIState.OnSuccess -> {
                            binding.progressBarDeatils.hideProgress()
                            val data = it.inventoryLevelRoot.inventory_level
                            this@ProductDetailsFragment.quantity = data.available.toString()

                        }

                        is InventoryLevelAPIState.OnFail -> {
                            binding.progressBarDeatils.hideProgress()
                            showToast(it.errorMessage.localizedMessage)
                        }

                        is InventoryLevelAPIState.Loading -> {
                            binding.progressBarDeatils.showProgress()
                        }
                    }
                }
            }
        }
    }

    private fun initImageRecycler(list: List<Image>) {
        imageAdapter.setList(list)
        binding.recyclerImage.adapter = imageAdapter
    }

    private fun initSizeRecycler(list: List<String>) {
        sizeAdapter.setList(list)
        binding.recyclerSize.adapter = sizeAdapter
    }

    private fun setVariantList(variantList: List<Variant>) {
        sizeAdapter.setVariantList(variantList)
    }

    private fun observeError() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailedProductViewModel.errorStateFlow.collect {
                    showToast(it)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}