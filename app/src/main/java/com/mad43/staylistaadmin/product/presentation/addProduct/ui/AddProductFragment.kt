package com.mad43.staylistaadmin.product.presentation.addProduct.ui

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentAddProductBinding
import com.mad43.staylistaadmin.product.data.entity.*
import com.mad43.staylistaadmin.product.data.remote.RemoteSource
import com.mad43.staylistaadmin.product.data.repo.Repo
import com.mad43.staylistaadmin.product.presentation.addProduct.viewModel.AddProductViewModel
import com.mad43.staylistaadmin.product.presentation.addProduct.viewModel.AddProductViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var addProductViewModelFactory: AddProductViewModelFactory
    private var variantList: MutableSet<Variant> = mutableSetOf()
    private var imageList: MutableList<Image> = mutableListOf()
    private var optionList: List<Option>? = null
    private var option1: Option? = null
    private var option2: Option? = null
    private lateinit var secondProductModel: SecondProductModel
    private lateinit var imageRoot: ImageRoot
    private var type = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        onClicks()
        option1 = Option(name = "Size")
        option2 = Option(name = "Color")
        optionList = listOf(option1 as Option, option2 as Option)
    }

    private fun initViewModel() {
        addProductViewModelFactory =
            AddProductViewModelFactory(Repo.getRepo(RemoteSource.getRemoteSource()))
        addProductViewModel =
            ViewModelProvider(this, addProductViewModelFactory)[AddProductViewModel::class.java]
    }

    private fun onClicks() {
        binding.apply {
            btnSaveProduct.setOnClickListener {
                type = autoCompleteTextViewtype.text.toString().trim()
                val title = editTextProductName.text.toString().trim()
                val poster = editTextPoster.text.toString().trim()
                val tags = editTextProductTags.text.toString().trim()
                val description = editTextProductDescription.text.toString().trim()
                val vendor = editTextProductVendorAdd.text.toString().trim()
                this@AddProductFragment.addProductViewModel.validateProduct(
                    title,
                    tags,
                    description,
                    poster,
                    vendor,
                    type
                )
                observeProductValidation(type, title, vendor, poster, tags, description)
            }

            autoCompleteTextViewtype.setOnItemClickListener { adapterView, view, i, l ->
                type = autoCompleteTextViewtype.text.toString().trim()
                this@AddProductFragment.addProductViewModel.setType(type)
                setSizeType()
            }

            btnNext.setOnClickListener {
                if (btnNext.isClickable) {
                    val quantity = binding.editTextProductQuantityAdd.text.toString().trim()
                    val size = binding.editTextProductSizeAdd.text.toString().trim()
                    val color = binding.autoCompleteTextViewColor.text.toString().trim()
                    val price = binding.editTextPriceAdd.text.toString().trim()
                    val q: Int = if (quantity.isEmpty()) {
                        0
                    } else {
                        quantity.toInt()
                    }
                    this@AddProductFragment.addProductViewModel.validateVariantData(
                        price,
                        quantity,
                        color,
                        size
                    )
                    observeVariantValidation(
                        price = price,
                        color = color,
                        size = size,
                        quantity = q
                    )
                } else {
                    showToast(getString(R.string.this_button_is_not_clickable))
                }
            }

            btnDone.setOnClickListener {
                binding.apply {
                    editTextProductSizeAdd.clearText()
                    autoCompleteTextViewColor.clearText()
                    editTextPriceAdd.clearText()
                    editTextProductQuantityAdd.clearText()
                    editTextProductSizeAdd.unEnableEditText()
                    autoCompleteTextViewColor.unEnableEditText()
                    editTextPriceAdd.unEnableEditText()
                    editTextProductQuantityAdd.unEnableEditText()
                    btnNext.disableButton()
                }

            }

            btnNextImage.setOnClickListener {
                if (btnDone.isClickable) {
                    val imageUrl = binding.editTextProductImageUriAdd.text.toString().trim()
                    this@AddProductFragment.addProductViewModel.validateImageUrl(imageUrl)
                    observeImageUrl(imageUrl)
                } else {
                    showToast(getString(R.string.this_button_is_not_clickable))
                }
            }

            imageViewBack.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }

            btnDoneImage.setOnClickListener {
                binding.editTextProductImageUriAdd.clearText()
                binding.editTextProductImageUriAdd.unEnableEditText()
                binding.btnNextImage.disableButton()
            }

        }
    }

    private fun setSizeType() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@AddProductFragment.addProductViewModel.type.collect {
                    if (it.equals("shoes", true)) {
                        binding.editTextProductSizeAdd.enableEditText()
                        binding.editTextProductSizeAdd.inputType =
                            InputType.TYPE_CLASS_NUMBER
                    } else if (it.equals("accessories", true)) {
                        binding.editTextProductSizeAdd.unEnableEditText()
                    } else {
                        binding.editTextProductSizeAdd.enableEditText()
                        binding.editTextProductSizeAdd.inputType =
                            InputType.TYPE_CLASS_TEXT
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setTypeArray()
        setColorArray()
        setCategoryArray()
    }

    private fun setCategoryArray() {
        val stringArrayColor = resources.getStringArray(R.array.categoryType)
        val categoryAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            R.layout.item_type_drop_down,
            stringArrayColor
        )
        binding.editTextProductTags.setAdapter(categoryAdapter)
    }

    private fun setColorArray() {
        val stringArrayColor = resources.getStringArray(R.array.color)
        val colorAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            R.layout.item_type_drop_down,
            stringArrayColor
        )
        binding.autoCompleteTextViewColor.setAdapter(colorAdapter)
    }

    private fun setTypeArray() {
        val stringArray = resources.getStringArray(R.array.typeArray)
        val typeAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            R.layout.item_type_drop_down,
            stringArray
        )
        binding.autoCompleteTextViewtype.setAdapter(typeAdapter)
    }

    private fun observeProductValidation(
        type: String,
        title: String,
        vendor: String,
        poster: String,
        tags: String,
        description: String
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addProductViewModel.validationStateFlow.collect {
                    when (it) {
                        is ValidateState.BeforeValidation -> {
                            binding.progressBarProduct.showProgress()
                        }

                        is ValidateState.OnValidateSuccess -> {
                            binding.progressBarProduct.hideProgress()
                            val product = Product(
                                vendor = vendor,
                                options = optionList,
                                tags = tags,
                                product_type = type,
                                title = title,
                                body_html = description,
                                variants = variantList.toList()
                            )
                            secondProductModel = SecondProductModel(product = product)
                            imageRoot = ImageRoot(image = Image(src = poster))
                            this@AddProductFragment.addProductViewModel.addProduct(
                                secondProductModel, imageRoot
                            )
                            observeAdding()
                        }

                        is ValidateState.OnValidateError -> {
                            binding.progressBarProduct.hideProgress()
                            when (it.place) {
                                Const.PRODUCT_NAME -> {
                                    binding.editTextProductName.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.PRODUCT_TYPE -> {
                                    binding.autoCompleteTextViewtype.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.VENDOR -> {
                                    binding.editTextProductVendorAdd.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.POSTER -> {
                                    binding.editTextPoster.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.TAGS -> {
                                    binding.editTextProductTags.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.DESCRIPTION -> {
                                    binding.editTextProductDescription.setErrorMessage(
                                        requireActivity().getString(it.message)
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

    }

    private fun observeAdding() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addProductViewModel.dataStateFlow.collect {
                    when (it) {
                        is ProductAPIState.OnSuccess -> {
                            binding.progressBarProduct.hideProgress()
                            val data = it.product
                            secondProductModel =
                                SecondProductModel(Product(images = imageList.toList()))
                            Log.e("TAG", imageList.toString())
                            this@AddProductFragment.addProductViewModel.updateProduct(
                                data.product.id as Long,
                                secondProductModel
                            )
                            observeUpdateImages()
                        }

                        is ProductAPIState.OnFail -> {
                            binding.progressBarProduct.hideProgress()
                            val message = it.errorMessage
                            showToast(message.message)
                        }

                        is ProductAPIState.Loading -> {
                            binding.progressBarProduct.showProgress()
                        }
                    }

                }
            }
        }

    }

    private fun observeVariantValidation(
        price: String,
        color: String,
        quantity: Int,
        size: String
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                addProductViewModel.validationStateFlow.collect {
                    when (it) {
                        is ValidateState.BeforeValidation -> {
                            binding.progressBarVariant.showProgress()
                        }

                        is ValidateState.OnValidateSuccess -> {
                            binding.progressBarVariant.hideProgress()
                            if (price.isNotEmpty() && color.isNotEmpty() && size.isNotEmpty() && quantity != 0) {
                                val variant = Variant(
                                    price = price,
                                    option1 = size,
                                    option2 = color,
                                    inventory_quantity = quantity
                                )
                                variantList.add(variant)
                                Log.e("TAG1", "pass")
                                Log.e("TAG1", variantList.toString())
                                binding.apply {
                                    editTextProductQuantityAdd.clearText()
                                    editTextProductSizeAdd.clearText()
                                    editTextPriceAdd.unEnableEditText()
                                    autoCompleteTextViewColor.unEnableEditText()
                                }
                            }
                        }

                        is ValidateState.OnValidateError -> {
                            binding.progressBarVariant.hideProgress()
                            when (it.place) {
                                Const.PRICE -> {
                                    binding.editTextPriceAdd.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.QUANTITY -> {
                                    binding.editTextProductQuantityAdd.setErrorMessage(
                                        requireActivity().getString(it.message)
                                    )
                                }
                                Const.COLOR -> {
                                    binding.autoCompleteTextViewColor.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                                Const.SIZE -> {
                                    binding.editTextProductSizeAdd.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private fun observeImageUrl(url: String) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addProductViewModel.validationStateFlow.collect {
                    when (it) {
                        is ValidateState.OnValidateSuccess -> {
                            binding.progressBarImage.hideProgress()
                            // navigate to back and add object to variant list
                            val image = Image(src = url)
                            imageList.add(image)
                            Log.e("TAG", imageList.toString())
                            binding.apply {
                                editTextProductImageUriAdd.clearText()
                            }
                        }

                        is ValidateState.OnValidateError -> {
                            binding.progressBarImage.hideProgress()
                            when (it.place) {
                                Const.IMAGE_URL -> {
                                    binding.editTextProductImageUriAdd.setErrorMessage(
                                        requireActivity().getString(it.message)
                                    )
                                }
                            }
                        }

                        is ValidateState.BeforeValidation -> {
                            binding.progressBarImage.showProgress()
                        }
                    }
                }
            }
        }
    }

    private fun observeUpdateImages() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addProductViewModel.validationStateFlow.collect {
                    when (it) {
                        is ValidateState.OnValidateSuccess -> {
                            binding.progressBarImage.hideProgress()
                            Log.e("TAG", "observeUpdateImages: 000000", )
                            Navigation.findNavController(this@AddProductFragment.requireView())
                                .navigateUp()
                        }

                        is ValidateState.OnValidateError -> {
                            binding.progressBarImage.hideProgress()
                        }

                        is ValidateState.BeforeValidation -> {
                            binding.progressBarImage.showProgress()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageList = mutableListOf()
        variantList = mutableSetOf()
        optionList = mutableListOf()
        _binding = null
    }
}