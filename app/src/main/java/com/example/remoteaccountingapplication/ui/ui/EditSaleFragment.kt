package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentEditSaleBinding
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import com.google.android.material.snackbar.Snackbar

class EditSaleFragment : Fragment() {

    private var _binding: FragmentEditSaleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RemoteAccountingViewModel by activityViewModels {
        RemoteAccountingViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository,
            activity?.application as RemoteAccountingApplication
        )
    }

    private val args: EditSaleFragmentArgs by navArgs()

    private var flagExist: Boolean = false
    private var currentProduct: String? = null
    private var physicalProduct: Boolean = false
    private var restoredRemains: Int = 0
    private var currentPrice: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditSaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val receivedBundle = savedInstanceState.getBundle("EditSaleBundle")
            if (receivedBundle != null) {
                flagExist = receivedBundle.getBoolean("flagExist")
                currentProduct = receivedBundle.getString("currentProduct")
                physicalProduct = receivedBundle.getBoolean("physicalProduct")
                restoredRemains = receivedBundle.getInt("restoredRemains")
                currentPrice = receivedBundle.getDouble("currentPrice")
                binding.productText.text = receivedBundle.getString("currentProduct")
                binding.priceText.text = receivedBundle.getDouble("currentPrice").toString()
            }
        } else if (args.itemId != -1) {

            viewModel.getSaleItemById(args.itemId).observe(this.viewLifecycleOwner) { salesItem ->

                binding.apply {
                    productText.text = salesItem.product
                    priceText.text = salesItem.price.toString()
                    numberText.setText(salesItem.number.toString())
                    paymentTypeText.setText(salesItem.paymentType)
                    saleTypeText.setText(salesItem.saleType)
                    nameText.setText(salesItem.name)
                    commentText.setText(salesItem.comment)
                    totalText.text = getString(
                        R.string.total_values,
                        (salesItem.price * salesItem.number).toString()
                    )
                }

                currentProduct = salesItem.product
                currentPrice = salesItem.price

                viewModel.ifProductExist(salesItem.product)
                    .observe(this.viewLifecycleOwner) { booleanAnswer ->

                        if (booleanAnswer) {

                            viewModel.getProductByProductName(salesItem.product)
                                .observe(this.viewLifecycleOwner) { productItem ->

                                    physicalProduct = productItem.physical

                                    if (productItem.physical) {
                                        binding.remainsCardText.text = buildString {
                                            append(getString(R.string.remains_word))
                                            append("\"${productItem.product}\" ")
                                            append(getString(R.string.remains_where))
                                            append("${productItem.remains}")
                                        }

                                        restoredRemains = productItem.remains + salesItem.number
                                    } else {
                                        binding.remainsCardText.text = getString(R.string.remains)
                                    }
                                }
                        } else {
                            Snackbar
                                .make(
                                    requireActivity().findViewById(android.R.id.content),
                                    getString(R.string.product_not_found),
                                    Snackbar.LENGTH_LONG
                                )
                                .show()

                            goBack()
                        }
                    }
            }
        }

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

        binding.numberText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val priceString = binding.priceText.text.toString()

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
                            val multiplication = currentPrice * number
                            binding.totalText.text =
                                getString(R.string.total_values, multiplication.toString())

                            val newRemains =
                                restoredRemains - number

                            if (newRemains >= 0) {
                                binding.remainsCardText.text = buildString {
                                    append(getString(R.string.remains_word))
                                    append("\"$currentProduct\" ")
                                    append(getString(R.string.remains_where))
                                    append("$newRemains")
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
                    } else {
                        binding.totalText.text = getString(R.string.total_values, priceString)

                        if (physicalProduct) {
                            binding.remainsCardText.text = buildString {
                                append(getString(R.string.remains_word))
                                append("\"$currentProduct\" ")
                                append(getString(R.string.remains_where))
                                append("$restoredRemains")
                            }
                        } else {
                            binding.remainsCardText.text = getString(R.string.remains)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            })

        binding.saveBtn.setOnClickListener {
            updateTables()
        }
    }

    private fun updateTables() {
        val productString = binding.productText.text.toString()
        val priceString = binding.priceText.text.toString()
        val numberString = binding.numberText.text.toString()
        val paymentTypeString = binding.paymentTypeText.text.toString()
        val saleTypeString = binding.saleTypeText.text.toString()
        val nameString = binding.nameText.text.toString()
        val commentString = binding.commentText.text.toString()

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


        if (numberString.isNotEmpty() && paymentTypeString.isNotEmpty() &&
            saleTypeString.isNotEmpty() && nameString.isNotEmpty()
        ) {
            val product = productString.trim().replace(",", "")
            val paymentType = paymentTypeString.trim().replace(",", "")
            val saleType = saleTypeString.trim().replace(",", "")
            val name = nameString.trim().replace(",", "")
            val comment = commentString.trim().replace(",", "")

            val price = priceString.trim().toDouble()
            val number = numberString.trim().toInt()

            val total = price * number
            val newRemainsProduct = restoredRemains - number

            viewModel.createNameRecordIfNotExist(name)
            viewModel.createPaymentTypeIfNotExist(paymentType)
            viewModel.createSaleTypeIfNotExist(saleType)

            viewModel.updateSaleItemById(
                args.itemId,
                product,
                price,
                number,
                paymentType,
                saleType,
                name,
                comment,
                total
            )

            if (physicalProduct) {
                viewModel.updateProductRemains(product, newRemainsProduct)
                goBack()
            } else {
                goBack()
            }
        }
    }

    private fun goBack() {
        view?.findNavController()
            ?.navigate(EditSaleFragmentDirections.actionEditSaleFragmentToReportFragment())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val bundle = Bundle()
        bundle.putBoolean("flagExist", flagExist)
        bundle.putString("currentProduct", currentProduct)
        bundle.putBoolean("physicalProduct", physicalProduct)
        bundle.putInt("restoredRemains", restoredRemains)
        bundle.putDouble("currentPrice", currentPrice)

        outState.putBundle("EditSaleBundle", bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}