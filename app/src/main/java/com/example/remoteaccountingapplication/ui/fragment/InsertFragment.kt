package com.example.remoteaccountingapplication.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentInsertBinding
import com.example.remoteaccountingapplication.ui.viewmodel.InsertFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.InsertFragmentViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InsertFragment : BaseFragment<FragmentInsertBinding>() {

    private val viewModel: InsertFragmentViewModel by viewModels {
        InsertFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private var flagExist: Boolean = false
    private var currentProduct: String? = null
    private var currentRemains: Int = 0
    private var physicalProduct: Boolean = false

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInsertBinding {
        return FragmentInsertBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val receivedBundle = savedInstanceState.getBundle("insertFragmentBundle")
            if (receivedBundle != null) {
                currentProduct = receivedBundle.getString("currentProduct")
                currentRemains = receivedBundle.getInt("currentRemains")
                physicalProduct = receivedBundle.getBoolean("physicalProduct")
                flagExist = receivedBundle.getBoolean("flagExist")
            }
        }

        viewModel.getNameByLastId().observe(this.viewLifecycleOwner) { name ->
            binding.nameText.setText(name)
        }

        val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val calendarInstance = Calendar.getInstance()

        val todayDate: String = simpleTodayDate.format(calendarInstance.time)
        val todayDateCalc: Long = calendarInstance.timeInMillis

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

                            if (productItem.physical) {
                                binding.remainsCardText.text = buildString {
                                    append(getString(R.string.remains_word))
                                    append("\"${productItem.product}\" ")
                                    append(getString(R.string.remains_where))
                                    append("${productItem.remains}")
                                }

                            } else {
                                binding.remainsCardText.text = getString(R.string.remains)
                            }
                            currentProduct = productItem.product
                            currentRemains = productItem.remains
                            physicalProduct = productItem.physical
                        } else {
                            currentRemains = 0
                            currentProduct = null
                        }
                    }
                }
        }

        binding.priceText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.totalText.text = getString(R.string.total_values, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.numberText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val priceString = binding.priceText.text.toString()

                if (currentProduct == binding.productText.text.toString()) {
                    if (!s.isNullOrEmpty()) {
                        val number = s.toString().trim().toInt()

                        if (number == 9999) {
                            Snackbar
                                .make(
                                    requireActivity().findViewById(android.R.id.content),
                                    getString(R.string.attention_max_number),
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        } else {

                            if (priceString.isNotEmpty()) {
                                val priceDouble = priceString.trim().toDouble()
                                val multiplication = priceDouble * number
                                binding.totalText.text =
                                    getString(R.string.total_values, multiplication.toString())
                            } else {
                                binding.totalText.text = getString(R.string.total)
                            }

                            if (currentRemains >= 0) {
                                val updatedRemains = currentRemains - number

                                if (updatedRemains >= 0) {
                                    binding.remainsCardText.text = buildString {
                                        append(getString(R.string.remains_word))
                                        append("\"$currentProduct\" ")
                                        append(getString(R.string.remains_where))
                                        append("$updatedRemains")
                                    }
                                } else {
                                    if (physicalProduct) {
                                        binding.remainsCardText.text = buildString {
                                            append(getString(R.string.remains_word))
                                            append("\"$currentProduct\" ")
                                            append(getString(R.string.remains_where_zero))
                                        }

                                        Snackbar
                                            .make(
                                                requireActivity().findViewById(android.R.id.content),
                                                getString(R.string.not_enough_products),
                                                Snackbar.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            }
                        }
                    } else {
                        binding.totalText.text = getString(R.string.total_values, priceString)
                        if (physicalProduct) {
                            binding.remainsCardText.text = buildString {
                                append(getString(R.string.remains_word))
                                append("\"$currentProduct\" ")
                                append(getString(R.string.remains_where))
                                append("$currentRemains")
                            }
                        } else {
                            binding.remainsCardText.text = getString(R.string.remains)
                        }
                    }
                } else {
                    currentProduct = null
                    currentRemains = 0
                    physicalProduct = false

                    binding.remainsCardText.text = getString(R.string.remains)

                    if (priceString.isNotEmpty()) {
                        if (!s.isNullOrEmpty()) {
                            val number = s.toString().trim().toInt()
                            val priceDouble = priceString.trim().toDouble()
                            val multiplication = priceDouble * number
                            binding.totalText.text =
                                getString(R.string.total_values, multiplication.toString())
                        } else {
                            binding.totalText.text =
                                getString(R.string.total_values, priceString)
                        }
                    } else {
                        binding.totalText.text = getString(R.string.total)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewModel.getPaymentTypes().observe(this.viewLifecycleOwner) { paymentTypesList ->
            val paymentTypes = paymentTypesList.map { it.paymentType }
            val autoCompleteAdapter: ArrayAdapter<String> =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, paymentTypes)
            binding.paymentTypeText.setAdapter(autoCompleteAdapter)
        }

        viewModel.getSaleTypes().observe(this.viewLifecycleOwner) { saleTypesList ->
            val saleTypes = saleTypesList.map { it.saleType }
            val autoCompleteAdapter: ArrayAdapter<String> =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, saleTypes)
            binding.saleTypeText.setAdapter(autoCompleteAdapter)
        }

        viewModel.getNames().observe(this.viewLifecycleOwner) { namesList ->
            val operatorNames = namesList.map { it.name }
            val autoCompleteAdapter: ArrayAdapter<String> =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, operatorNames)
            binding.nameText.setAdapter(autoCompleteAdapter)
        }

        binding.saveBtn.setOnClickListener {

            val productString = binding.productText.text.toString()
            val priceString = binding.priceText.text.toString()
            val numberString = binding.numberText.text.toString()
            val paymentTypeString = binding.paymentTypeText.text.toString()
            val saleTypeString = binding.saleTypeText.text.toString()
            val nameString = binding.nameText.text.toString()
            val commentString = binding.commentText.text.toString()

            if (productString.isEmpty()) {
                binding.productLayout.error = getString(R.string.can_not_be_empty)
                binding.productLayout.isErrorEnabled = true
            } else {
                binding.productLayout.isErrorEnabled = false
            }

            if (priceString.isEmpty()) {
                binding.priceLayout.error = getString(R.string.can_not_be_empty)
                binding.priceLayout.isErrorEnabled = true
            } else {
                binding.priceLayout.isErrorEnabled = false
            }

            if (numberString.isEmpty()) {
                binding.numberLayout.error = getString(R.string.can_not_be_empty)
                binding.numberLayout.isErrorEnabled = true
            } else {
                binding.numberLayout.isErrorEnabled = false
            }

            if (paymentTypeString.isEmpty()) {
                binding.paymentTypeLayout.error = getString(R.string.can_not_be_empty)
                binding.paymentTypeLayout.isErrorEnabled = true
            } else {
                binding.paymentTypeLayout.isErrorEnabled = false
            }

            if (saleTypeString.isEmpty()) {
                binding.saleTypeLayout.error = getString(R.string.can_not_be_empty)
                binding.saleTypeLayout.isErrorEnabled = true
            } else {
                binding.saleTypeLayout.isErrorEnabled = false
            }

            if (nameString.isEmpty()) {
                binding.nameLayout.error = getString(R.string.can_not_be_empty)
                binding.nameLayout.isErrorEnabled = true
            } else {
                binding.nameLayout.isErrorEnabled = false
            }

            if (productString.isNotEmpty() && priceString.isNotEmpty() && numberString.isNotEmpty() &&
                paymentTypeString.isNotEmpty() && saleTypeString.isNotEmpty() && nameString.isNotEmpty()
            ) {

                val product = productString.trim().replace(",", "")
                val paymentType = paymentTypeString.trim().replace(",", "")
                val saleType = saleTypeString.trim().replace(",", "")
                val name = nameString.trim().replace(",", "")
                val comment = commentString.trim().replace(",", "")

                val price = priceString.trim().toDouble()
                val number = numberString.trim().toInt()

                val total = price * number

                viewModel.createNameRecordIfNotExist(name)
                viewModel.createPaymentTypeIfNotExist(paymentType)
                viewModel.createSaleTypeIfNotExist(saleType)

                viewModel.createSale(
                    todayDateCalc,
                    todayDate,
                    product,
                    price,
                    number,
                    paymentType,
                    saleType,
                    name,
                    comment,
                    total
                )

                if (currentProduct == product) {
                    if (currentRemains > 0) {
                        val updatedRemainsProductTable = currentRemains - number
                        viewModel.updateProductItem(
                            product,
                            price,
                            updatedRemainsProductTable,
                            true
                        )
                    } else {
                        viewModel.updateProductItem(product, price, 0, true)
                    }
                    clearFields()
                } else {
                    viewModel.createProductNote(product, price)
                    clearFields()
                }
            }

            Snackbar
                .make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.sale_saved),
                    Snackbar.LENGTH_SHORT
                )
                .show()
        }
    }

    private fun clearFields() {
        binding.apply {
            productText.text?.clear()
            priceText.text?.clear()
            numberText.text?.clear()
            paymentTypeText.text?.clear()
            saleTypeText.text?.clear()
            commentText.text?.clear()
            totalText.text = getString(R.string.total)
            remainsCardText.text = getString(R.string.remains)
        }
        currentProduct = null
        currentRemains = 0
        physicalProduct = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        bundle.putString("currentProduct", currentProduct)
        bundle.putInt("currentRemains", currentRemains)
        bundle.putBoolean("physicalProduct", physicalProduct)
        bundle.putBoolean("flagExist", flagExist)

        outState.putBundle("insertFragmentBundle", bundle)
    }
}