package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentReceiptBinding
import com.example.remoteaccountingapplication.ui.recyclerview.ReceiptRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.viewmodel.ReceiptFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ReceiptFragmentViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReceiptFragment : Fragment() {

    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReceiptFragmentViewModel by viewModels {
        ReceiptFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private val args: ReceiptFragmentArgs by navArgs()
    private lateinit var adapter: ReceiptRecyclerViewAdapter

    private var flagExist: Boolean = false
    private var currentProduct: String? = null
    private var currentRemains: Int = 0
    private var currentDay: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val receivedBundle = savedInstanceState.getBundle("receiptFragmentBundle")
            if (receivedBundle != null) {
                currentProduct = receivedBundle.getString("currentProduct")
                currentRemains = receivedBundle.getInt("currentRemains")
                currentDay = receivedBundle.getInt("currentDay")
                flagExist = receivedBundle.getBoolean("flagExist")
            }
        }

        val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val calendarInstance = Calendar.getInstance()

        val todayDate: String = simpleTodayDate.format(calendarInstance.time)
        val todayDateCalc: Long = calendarInstance.timeInMillis

        currentDay = calendarInstance.get(Calendar.DAY_OF_MONTH)
        val startOfToday = getStartOfTodayInMillis(currentDay)
        val endOfToday = getEndOfTodayInMillis(currentDay)

        binding.dateText.text = getString(R.string.today_date, todayDate)
        binding.nameCardText.text = getString(R.string.name_title_with_name, args.name)

        viewModel.createNameRecordIfNotExist(args.name)

        viewModel.getProducts().observe(this.viewLifecycleOwner) { productsList ->
            val productsNames = productsList.map { it.product }
            val autoCompleteAdapter: ArrayAdapter<String> =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, productsNames)
            binding.productText.setAdapter(autoCompleteAdapter)
        }

        binding.productText.setOnItemClickListener { parent, _, position, _ ->
            val selectedProductName = parent.getItemAtPosition(position) as String

            flagExist = false

            viewModel.getProductByProductName(selectedProductName)
                .observe(this.viewLifecycleOwner) { productItem ->

                    if (productItem != null) {
                        if (productItem.product == binding.productText.text.toString()) {
                            binding.priceText.setText(productItem.price.toString())

                            binding.remainsCardText.text = buildString {
                                append(getString(R.string.remains_word))
                                append("\"${productItem.product}\" ")
                                append(getString(R.string.remains_where))
                                append("${productItem.remains}")
                            }

                            currentProduct = productItem.product
                            currentRemains = productItem.remains

                        } else {
                            currentRemains = 0
                            currentProduct = null
                        }

                    }
                }
        }

        binding.numberText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (currentProduct == binding.productText.text.toString()) {
                    if (!s.isNullOrEmpty()) {
                        if (s[0] == '-') {
                            val numberString = s.toString().trim().replace("-", "")

                            if (numberString != "") {
                                val number = numberString.toInt()
                                val remains = currentRemains - number
                                if (remains < 0) {
                                    Snackbar
                                        .make(
                                            requireActivity().findViewById(android.R.id.content),
                                            getString(R.string.attention_negative_number),
                                            Snackbar.LENGTH_LONG
                                        )
                                        .show()
                                }
                                binding.remainsCardText.text = buildString {
                                    append(getString(R.string.remains_word))
                                    append("\"$currentProduct\" ")
                                    append(getString(R.string.remains_where))
                                    append("$remains")
                                }
                            }

                        } else {
                            val number = s.toString().trim().toInt()
                            val remains = currentRemains + number
                            binding.remainsCardText.text = buildString {
                                append(getString(R.string.remains_word))
                                append("\"$currentProduct\" ")
                                append(getString(R.string.remains_where))
                                append("$remains")
                            }
                        }
                    } else {
                        binding.remainsCardText.text = buildString {
                            append(getString(R.string.remains_word))
                            append("\"$currentProduct\" ")
                            append(getString(R.string.remains_where))
                            append("$currentRemains")
                        }
                    }
                } else {
                    currentProduct = null
                    currentRemains = 0
                    if (binding.productText.text.isNullOrEmpty()) {
                        binding.remainsCardText.text = getString(R.string.remains)
                    } else {
                        binding.remainsCardText.text = buildString {
                            append(getString(R.string.remains_word))
                            append("\"${binding.productText.text}\" ")
                            append(getString(R.string.remains_where_zero))
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.addBtn.setOnClickListener {

            val nameString = args.name
            val productString = binding.productText.text.toString()
            val numberString = binding.numberText.text.toString()
            val priceString = binding.priceText.text.toString()

            if (productString.isEmpty()) {
                binding.productLayout.error = getString(R.string.can_not_be_empty)
                binding.productLayout.isErrorEnabled = true
            } else {
                binding.productLayout.isErrorEnabled = false
            }

            if (numberString.isEmpty()) {
                binding.numberLayout.error = getString(R.string.can_not_be_empty)
                binding.numberLayout.isErrorEnabled = true
            } else {
                binding.numberLayout.isErrorEnabled = false
            }

            if (productString.isNotEmpty() && numberString.isNotEmpty()) {

                var price = 0.0
                var remains = 0

                if (priceString.isNotEmpty()) {
                    price = priceString.trim().toDouble()
                }

                val name = nameString.trim().replace(",", "")
                val product = productString.trim().replace(",", "")
                val number = numberString.trim().toInt()

                if (currentProduct == product) {
                    remains = currentRemains
                }

                if (number != 0) {
                    val newNumber = remains + number

                    if (newNumber >= 0) {

                        viewModel.createUpdateProductWithRemains(product, price, newNumber, true)

                        viewModel.createReceiptItem(
                            todayDateCalc,
                            todayDate,
                            name,
                            product,
                            price,
                            number
                        )

                        clearFields()
                    } else {
                        Snackbar
                            .make(
                                requireActivity().findViewById(android.R.id.content),
                                getString(R.string.can_t_save_item_with_negative_number),
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                } else {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.product_number_can_t_be_zero),
                            Snackbar.LENGTH_LONG
                        )
                        .show()
                }
            }
        }

        viewModel.getListOfReceiptByDate(startOfToday, endOfToday)
            .observe(this.viewLifecycleOwner) { receiptList ->
                adapter = ReceiptRecyclerViewAdapter(receiptList)
                binding.recyclerViewReport.adapter = adapter
            }
    }

    private fun clearFields() {
        binding.apply {
            productText.text?.clear()
            numberText.text?.clear()
            priceText.text?.clear()
            remainsCardText.text = getString(R.string.remains)
        }
        currentProduct = null
        currentRemains = 0
    }

    private fun getStartOfTodayInMillis(day: Int): Long {
        val calendarInstance = Calendar.getInstance()
        calendarInstance.set(Calendar.DAY_OF_MONTH, day)
        calendarInstance.set(Calendar.HOUR_OF_DAY, 0)
        calendarInstance.set(Calendar.MINUTE, 0)
        calendarInstance.set(Calendar.SECOND, 0)
        calendarInstance.set(Calendar.MILLISECOND, 0)
        return calendarInstance.timeInMillis
    }

    private fun getEndOfTodayInMillis(day: Int): Long {
        val calendarInstance = Calendar.getInstance()
        calendarInstance.set(Calendar.DAY_OF_MONTH, day)
        calendarInstance.set(Calendar.HOUR_OF_DAY, 23)
        calendarInstance.set(Calendar.MINUTE, 59)
        calendarInstance.set(Calendar.SECOND, 59)
        calendarInstance.set(Calendar.MILLISECOND, 999)
        return calendarInstance.timeInMillis
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        bundle.putString("currentProduct", currentProduct)
        bundle.putInt("currentRemains", currentRemains)
        bundle.putInt("currentDay", currentDay)
        bundle.putBoolean("flagExist", flagExist)

        outState.putBundle("receiptFragmentBundle", bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}