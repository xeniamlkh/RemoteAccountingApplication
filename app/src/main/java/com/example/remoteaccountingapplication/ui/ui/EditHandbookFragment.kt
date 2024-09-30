package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentEditHandbookBinding
import com.example.remoteaccountingapplication.ui.viewmodel.EditHandbookFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.EditHandbookFragmentViewModelFactory
import com.google.android.material.snackbar.Snackbar

class EditHandbookFragment : Fragment() {

    private var _binding: FragmentEditHandbookBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditHandbookFragmentViewModel by viewModels {
        EditHandbookFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private val args: EditHandbookFragmentArgs by navArgs()
    private var physicalProduct: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditHandbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.tableId) {
            2 -> setProduct()
            3 -> setPaymentType()
            4 -> setSaleType()
            5 -> setName()
            else -> Snackbar
                .make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.no_item),
                    Snackbar.LENGTH_SHORT
                )
                .show()
        }

        binding.saveBtn.setOnClickListener {
            when (args.tableId) {
                2 -> saveProduct()
                3 -> savePaymentType()
                4 -> saveSaleType()
                5 -> saveName()
                else -> Snackbar
                    .make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.no_item),
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }

    private fun setProduct() {
        binding.apply {
            item.visibility = View.VISIBLE
            itemEdit.visibility = View.VISIBLE

            price.visibility = View.VISIBLE
            priceEdit.visibility = View.VISIBLE

            remainsUnchanged.visibility = View.VISIBLE
            remainsTextUnchanged.visibility = View.VISIBLE

            item.hint = getString(R.string.product_asterisk)
            price.hint = getString(R.string.price_asterisk)
        }

        if (args.itemId != -1) {
            viewModel.getProductById(args.itemId).observe(this.viewLifecycleOwner) { product ->
                binding.apply {
                    itemEdit.setText(product.product)
                    priceEdit.setText(product.price.toString())
                    remainsTextUnchanged.text = product.remains.toString()
                }
                physicalProduct = product.physical
            }
        }
    }

    private fun setPaymentType() {

        binding.apply {
            item.visibility = View.VISIBLE
            itemEdit.visibility = View.VISIBLE

            price.visibility = View.GONE
            priceEdit.visibility = View.GONE

            remainsUnchanged.visibility = View.GONE
            remainsTextUnchanged.visibility = View.GONE
            item.hint = getString(R.string.payment_type_asterisk)
        }

        if (args.itemId != -1) {
            viewModel.getPaymentTypeById(args.itemId)
                .observe(this.viewLifecycleOwner) { paymentType ->
                    binding.itemEdit.setText(paymentType.paymentType)
                }
        }
    }

    private fun setSaleType() {

        binding.apply {
            item.visibility = View.VISIBLE
            itemEdit.visibility = View.VISIBLE

            price.visibility = View.GONE
            priceEdit.visibility = View.GONE

            remainsUnchanged.visibility = View.GONE
            remainsTextUnchanged.visibility = View.GONE
            item.hint = getString(R.string.sale_type_asterisk)
        }

        if (args.itemId != -1) {
            viewModel.getSaleTypeById(args.itemId).observe(this.viewLifecycleOwner) { saleType ->
                binding.itemEdit.setText(saleType.saleType)
            }
        }
    }

    private fun setName() {

        binding.apply {
            item.visibility = View.VISIBLE
            itemEdit.visibility = View.VISIBLE

            price.visibility = View.GONE
            priceEdit.visibility = View.GONE

            remainsUnchanged.visibility = View.GONE
            remainsTextUnchanged.visibility = View.GONE
            item.hint = getString(R.string.name_asterisk)
        }

        if (args.itemId != -1) {
            viewModel.getNameById(args.itemId).observe(this.viewLifecycleOwner) { name ->
                binding.itemEdit.setText(name.name)
            }
        }
    }

    private fun saveProduct() {

        val itemString: String = binding.itemEdit.text.toString()
        val priceString: String = binding.priceEdit.text.toString()
        val remainsString: String = binding.remainsTextUnchanged.text.toString()

        if (itemString.isNotEmpty() && priceString.isNotEmpty()) {

            val item = itemString.trim().replace(",", "")
            val price = priceString.trim().toDouble()
            val remains = remainsString.trim().toInt()

            if (physicalProduct) {
                viewModel.updateProductById(args.itemId, item, price, remains, true)
            } else {
                viewModel.updateProductById(args.itemId, item, price, 0, false)
            }
        }

        goBack()
    }

    private fun savePaymentType() {
        val itemString: String = binding.itemEdit.text.toString()

        if (itemString.isNotEmpty()) {
            val item = itemString.trim().replace(",", "")
            viewModel.updatePaymentTypeById(args.itemId, item)
        }

        goBack()
    }

    private fun saveSaleType() {
        val itemString: String = binding.itemEdit.text.toString()

        if (itemString.isNotEmpty()) {
            val item = itemString.trim().replace(",", "")
            viewModel.updateSaleTypeById(args.itemId, item)
        }

        goBack()
    }

    private fun saveName() {
        val itemString: String = binding.itemEdit.text.toString()

        if (itemString.isNotEmpty()) {
            val item = itemString.trim().replace(",", "")
            viewModel.updateNameById(args.itemId, item)
        }

        goBack()
    }

    private fun goBack() {

        when (args.tableId) {
            2 -> view?.findNavController()
                ?.navigate(
                    EditHandbookFragmentDirections
                        .actionEditHandbookFragmentToProductsFragment()
                )

            3 -> view?.findNavController()
                ?.navigate(
                    EditHandbookFragmentDirections
                        .actionEditHandbookFragmentToPaymentTypesFragment()
                )

            4 -> view?.findNavController()
                ?.navigate(
                    EditHandbookFragmentDirections
                        .actionEditHandbookFragmentToSaleTypesFragment()
                )

            5 -> view?.findNavController()
                ?.navigate(
                    EditHandbookFragmentDirections
                        .actionEditHandbookFragmentToNamesFragment()
                )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}