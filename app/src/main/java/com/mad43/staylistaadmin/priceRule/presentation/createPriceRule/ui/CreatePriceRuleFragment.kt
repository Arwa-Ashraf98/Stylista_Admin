package com.mad43.staylistaadmin.priceRule.presentation.createPriceRule.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentCreatePriceRuleBinding
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRule
import com.mad43.staylistaadmin.priceRule.data.entity.PriceRuleRoot
import com.mad43.staylistaadmin.priceRule.data.remote.PriceRuleRemoteSource
import com.mad43.staylistaadmin.priceRule.data.repo.PriceRuleRepo
import com.mad43.staylistaadmin.priceRule.presentation.createPriceRule.viewModel.CreatePriceRuleViewModel
import com.mad43.staylistaadmin.priceRule.presentation.createPriceRule.viewModel.CreatePriceViewModelFactory
import com.mad43.staylistaadmin.utils.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreatePriceRuleFragment : Fragment() {
    private var _binding: FragmentCreatePriceRuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var createPriceRuleViewModel: CreatePriceRuleViewModel
    private lateinit var viewModelFactory: CreatePriceViewModelFactory
    private var typeValue = ""
    private lateinit var priceRule: PriceRule
    private lateinit var builder: MaterialDatePicker.Builder<*>
    private lateinit var picker: MaterialDatePicker<*>
    private lateinit var calendarConstraintBuilder: CalendarConstraints.Builder
    private lateinit var calendar: Calendar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePriceRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        builder = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        initDatePicker()
        onClicks()

    }

    private fun setDefaultLanguage(context: Context, lang: String?) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

    private fun initViewModel() {
        val remoteSource = PriceRuleRemoteSource()
        val repo = PriceRuleRepo(remoteSource)
        viewModelFactory =
            CreatePriceViewModelFactory(repo)
        createPriceRuleViewModel =
            ViewModelProvider(this, viewModelFactory)[CreatePriceRuleViewModel::class.java]
    }

    private fun onClicks() {
        binding.apply {
            btnCreatePriceRule.setOnClickListener {
                getData()
            }

            imageViewBack.setOnClickListener {
                Navigation.findNavController(
                    it
                ).navigateUp()
            }

            editTextStartsAt.setOnClickListener {
                showDatePicker(true, it as EditText)
                picker.show(requireActivity().supportFragmentManager, "DATE_PICKER1")
            }

            editTextEndDate.setOnClickListener {
                showDatePicker(false, it as EditText)
                picker.show(requireActivity().supportFragmentManager, "DATE_PICKER2")
            }

            autoCompleteTextViewPriceRuleType.setOnItemClickListener { adapterView, view, i, l ->
                typeValue = autoCompleteTextViewPriceRuleType.text.toString().trim()
            }

        }
    }

    private fun initDatePicker() {
        setDefaultLanguage(requireContext(), "en")
        builder.setTitleText("Select Date")
        builder.setTheme(R.style.ThemeOverlay_App_MaterialCalendar)
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        calendarConstraintBuilder = CalendarConstraints.Builder()
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.MONTH, Calendar.JUNE)
        val june: Long = calendar.timeInMillis
        calendar.set(Calendar.MONTH, Calendar.JULY)
        val july: Long = calendar.timeInMillis
        calendarConstraintBuilder.setStart(june)
        calendarConstraintBuilder.setEnd(july)
        calendarConstraintBuilder.setFirstDayOfWeek(Calendar.SATURDAY)
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(calendarConstraintBuilder.build())
        picker = builder.build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePicker(isFrom: Boolean, editText: EditText) {
        picker.addOnPositiveButtonClickListener {
            Log.e("TAG", picker.headerText)
            val format1 = SimpleDateFormat("yyyy-MM-dd")
            val formatted: String = format1.format(calendar.time)
            Log.e("TAG11", formatted)
            editText.setText(formatted)
            if (!isFrom) {
                val afterOneDay =
                    MaterialDatePicker.todayInUtcMilliseconds().plus(24 * 60 * 60 * 60)
                builder = MaterialDatePicker.Builder.datePicker().setSelection(afterOneDay)
                builder.setTitleText("Select Date")
                builder.setTheme(R.style.ThemeOverlay_App_MaterialCalendar)
                builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                calendarConstraintBuilder.setValidator(DateValidatorPointForward.from(afterOneDay))
                builder.setCalendarConstraints(calendarConstraintBuilder.build())
            }
        }

        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
    }

    private fun getData() {
        binding.apply {
            val title = editTextPriceRuleTitle.text.toString().trim()
            val startDate = editTextStartsAt.text.toString().trim()
            val endData = editTextEndDate.text.toString().trim()
            val value = editTextValue.text.toString().trim()
            val limit = editTextLimit.text.toString().trim()
            this@CreatePriceRuleFragment.createPriceRuleViewModel.validateData(
                title,
                value,
                typeValue,
                startDate,
                endData,
                limit
            )
            observeValidation(title, value, typeValue, startDate, endData, limit)

        }
    }

    private fun observeValidation(
        title: String,
        value: String,
        valueType: String,
        startData: String,
        endDate: String,
        limit: String
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@CreatePriceRuleFragment.createPriceRuleViewModel.validationStateFlow.collect {
                    when (it) {
                        is ValidateState.BeforeValidation -> {
                            Log.e("TAG", "observeValidation: b ")
                        }

                        is ValidateState.OnValidateSuccess -> {
                            priceRule = PriceRule(
                                title = title,
                                value = value,
                                value_type = valueType,
                                starts_at = startData,
                                ends_at = endDate,
                                usage_limit = limit.toInt()
                            )
                            val priceRuleRoot = PriceRuleRoot(priceRule)
                            this@CreatePriceRuleFragment.createPriceRuleViewModel.createPriceRule(
                                priceRuleRoot
                            )
                            observePriceRuleCreation()
                        }

                        is ValidateState.OnValidateError -> {
                            when (it.place) {
                                Const.TITLE -> {
                                    binding.editTextPriceRuleTitle.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }

                                Const.VALUE -> {
                                    binding.editTextValue.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }

                                Const.VALUE_TYPE -> {
                                    binding.autoCompleteTextViewPriceRuleType.setErrorMessage(
                                        requireActivity().getString(it.message)
                                    )
                                }

                                Const.START_DATE -> {
                                    binding.editTextStartsAt.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }

                                Const.END_DATE -> {
                                    binding.editTextEndDate.setErrorMessage(
                                        requireActivity().getString(
                                            it.message
                                        )
                                    )
                                }

                                Const.LIMIT -> {
                                    binding.editTextLimit.setErrorMessage(
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

    private fun observePriceRuleCreation() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createPriceRuleViewModel.priceRuleStateFlow.collect {
                    when (it) {
                        is PriceRuleCreationAPIState.Loading -> {

                        }

                        is PriceRuleCreationAPIState.OnSuccess -> {
                            Navigation.findNavController(requireView()).navigateUp()
                        }

                        is PriceRuleCreationAPIState.OnFail -> {
                            showToast(it.errorMessage.localizedMessage)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setType()
    }

    private fun setType() {
        val stringArray = resources.getStringArray(R.array.priceRuleType)
        val typeAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            R.layout.item_type_drop_down,
            stringArray
        )
        binding.autoCompleteTextViewPriceRuleType.setAdapter(typeAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}