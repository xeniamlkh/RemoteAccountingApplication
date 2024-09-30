package com.example.remoteaccountingapplication.ui.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.activityViewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentRestoreBinding
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.NumberFormatException

class RestoreFragment : Fragment() {

    private var _binding: FragmentRestoreBinding? = null
    private val binding get() = _binding!!

    private var currentRequestCode: Int = 0

    var salesImported: Boolean = false
    var productsImported: Boolean = false
    var paymentTypesImported: Boolean = false
    var saleTypesImported: Boolean = false
    var namesImported: Boolean = false

    private val viewModel: RemoteAccountingViewModel by activityViewModels {
        RemoteAccountingViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository,
            activity?.application as RemoteAccountingApplication
        )
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val type = data?.resolveType(requireContext())

                if (type.equals("text/comma-separated-values")) {
                    data?.data?.let { uri ->

                        when (currentRequestCode) {
                            REQUEST_CODE_SALES -> importSalesCsv(uri)
                            REQUEST_CODE_PRODUCTS -> importProductsCsv(uri)
                            REQUEST_CODE_PAYMENT_TYPES -> importPaymentTypesCsv(uri)
                            REQUEST_CODE_SALE_TYPES -> importSaleTypesCsv(uri)
                            REQUEST_CODE_NAMES -> importNamesCsv(uri)
                            REQUEST_CODE_RECEIPT -> importReceiptOfGoodsCsv(uri)
                            else -> Snackbar
                                .make(
                                    requireActivity().findViewById(android.R.id.content),
                                    getString(R.string.error_can_t_import_table),
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        }

                    }
                } else {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_mime_type_choose_another_file),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSalesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.salesRows.text = number.toString()
        }

        viewModel.getProductsRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.productsRows.text = number.toString()
        }

        viewModel.getPaymentTypesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.paymentTypesRows.text = number.toString()
        }

        viewModel.getSaleTypesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.saleTypesRows.text = number.toString()
        }

        viewModel.getNamesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.namesRows.text = number.toString()
        }

        // !!!!!!!
        viewModel.getReceiptRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.receiptRows?.text = number.toString()
        }

        binding.restoreSalesBtn.setOnClickListener {
            restoreSalesTable()
        }

        binding.restoreProductsBtn.setOnClickListener {
            restoreProductsTable()
        }

        binding.restorePaymentTypesBtn.setOnClickListener {
            restorePaymentTypesTable()
        }

        binding.restoreSaleTypesBtn.setOnClickListener {
            restoreSaleTypesTable()
        }

        binding.restoreNamesBtn.setOnClickListener {
            restoreNamesTable()
        }

        binding.restoreReceiptBtn?.setOnClickListener {
            restoreReceiptTable()
        }
    }

    private fun restoreSalesTable() {
        currentRequestCode = REQUEST_CODE_SALES
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun restoreProductsTable() {
        currentRequestCode = REQUEST_CODE_PRODUCTS
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun restorePaymentTypesTable() {
        currentRequestCode = REQUEST_CODE_PAYMENT_TYPES
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun restoreSaleTypesTable() {
        currentRequestCode = REQUEST_CODE_SALE_TYPES
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun restoreNamesTable() {
        currentRequestCode = REQUEST_CODE_NAMES
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun restoreReceiptTable() {
        currentRequestCode = REQUEST_CODE_RECEIPT
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        resultLauncher.launch(intent)
    }

    private fun importSalesCsv(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {

            val fileName = DocumentFile.fromSingleUri(requireContext(), uri)?.name

            if (fileName != null) {

                if (!fileName.contains("Sales")) {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_table_choose_a_sales_table),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))

                    bufferedReader.useLines { lines ->
                        lines.forEach { line ->
                            val tokens = line.split(",")
                            if (tokens.size == 10) {
                                val dateCalculationString = tokens[0].trim()
                                val dateString = tokens[1].trim()
                                val productString = tokens[2].trim()
                                val priceString = tokens[3].trim()
                                val numberString = tokens[4].trim()
                                val paymentTypeString = tokens[5].trim()
                                val saleTypeString = tokens[6].trim()
                                val nameString = tokens[7].trim()
                                val commentString = tokens[8].trim()
                                val totalString = tokens[9].trim()

                                try {
                                    val dateCalculationLong = dateCalculationString.toLong()
                                    val priceDouble = priceString.toDouble()
                                    val numberInt = numberString.toInt()
                                    val totalDouble = totalString.toDouble()

                                    viewModel.createSale(
                                        dateCalculationLong,
                                        dateString,
                                        productString,
                                        priceDouble,
                                        numberInt,
                                        paymentTypeString,
                                        saleTypeString,
                                        nameString,
                                        commentString,
                                        totalDouble
                                    )
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.sales_table_imported),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    requireActivity().runOnUiThread {
                        binding.salesCheck.visibility = View.VISIBLE
                    }

                }
            }
        }

        viewModel.getSalesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.salesRows.text = number.toString()
        }
    }

    private fun importProductsCsv(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {

            val fileName = DocumentFile.fromSingleUri(requireContext(), uri)?.name

            if (fileName != null) {

                if (!fileName.contains("Products")) {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_table_choose_a_products_table),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))

                    bufferedReader.useLines { lines ->
                        lines.forEach { line ->
                            val tokens = line.split(",")
                            if (tokens.size == 4) {
                                val productString = tokens[0].trim()
                                val priceString = tokens[1].trim()
                                val remainsString = tokens[2].trim()
                                val physicalString = tokens[3].trim()

                                try {
                                    val priceDouble = priceString.toDouble()
                                    val remainsInt = remainsString.toInt()
                                    val physicalBoolean = physicalString.toBoolean()

                                    viewModel.createProductNoteWithRemains(
                                        productString,
                                        priceDouble,
                                        remainsInt,
                                        physicalBoolean
                                    )
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.products_table_imported),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    requireActivity().runOnUiThread {
                        binding.productsCheck.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.getProductsRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.productsRows.text = number.toString()
        }
    }

    private fun importPaymentTypesCsv(uri: Uri) {

        CoroutineScope(Dispatchers.IO).launch {

            val fileName = DocumentFile.fromSingleUri(requireContext(), uri)?.name

            if (fileName != null) {

                if (!fileName.contains("Payment Types")) {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_table_choose_a_payment_types_table),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))

                    bufferedReader.useLines { lines ->
                        lines.forEach { line ->
                            val tokens = line.split(",")
                            if (tokens.size == 1) {
                                val paymentTypeString = tokens[0].trim()
                                try {
                                    viewModel.createPaymentTypeRecord(paymentTypeString)
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.payment_types_table_imported),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    requireActivity().runOnUiThread {
                        binding.paymentTypesCheck.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.getPaymentTypesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.paymentTypesRows.text = number.toString()
        }
    }

    private fun importSaleTypesCsv(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {

            val fileName = DocumentFile.fromSingleUri(requireContext(), uri)?.name

            if (fileName != null) {

                if (!fileName.contains("Sale Types")) {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_table_choose_a_sale_types_table),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))

                    bufferedReader.useLines { lines ->
                        lines.forEach { line ->
                            val tokens = line.split(",")
                            if (tokens.size == 1) {
                                val saleTypeString = tokens[0].trim()
                                try {
                                    viewModel.createSaleTypeRecord(saleTypeString)
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.sale_types_table_imported),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    requireActivity().runOnUiThread {
                        binding.saleTypesCheck.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.getSaleTypesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.saleTypesRows.text = number.toString()
        }
    }

    private fun importNamesCsv(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {

            val fileName = DocumentFile.fromSingleUri(requireContext(), uri)?.name

            if (fileName != null) {

                if (!fileName.contains("Names")) {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_table_choose_a_names_table),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))

                    bufferedReader.useLines { lines ->
                        lines.forEach { line ->
                            val tokens = line.split(",")
                            if (tokens.size == 1) {
                                val nameString = tokens[0].trim()

                                try {
                                    viewModel.createNameRecord(nameString)
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.names_table_imported),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    requireActivity().runOnUiThread {
                        binding.namesCheck.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.getNamesRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.namesRows.text = number.toString()
        }
    }

    private fun importReceiptOfGoodsCsv(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {

            val fileName = DocumentFile.fromSingleUri(requireContext(), uri)?.name

            if (fileName != null) {

                if (!fileName.contains("ReceiptOfGoods")) {
                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_wrong_table_choose_an_receipt_table),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-16"))

                    bufferedReader.useLines { lines ->
                        lines.forEach { line ->
                            val tokens = line.split(",")
                            if (tokens.size == 6) {
                                val dateCalculationString = tokens[0].trim()
                                val dateString = tokens[1].trim()
                                val nameString = tokens[2].trim()
                                val productString = tokens[3].trim()
                                val priceString = tokens[4].trim()
                                val numberString = tokens[5].trim()

                                try {
                                    val dateCalculationLong = dateCalculationString.toLong()
                                    val priceDouble = priceString.toDouble()
                                    val numberInt = numberString.toInt()

                                    viewModel.createReceiptItem(
                                        dateCalculationLong,
                                        dateString,
                                        nameString,
                                        productString,
                                        priceDouble,
                                        numberInt
                                    )
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    Snackbar
                        .make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.receipt_table_imported),
                            Snackbar.LENGTH_SHORT
                        )
                        .show()

                    requireActivity().runOnUiThread {
                        binding.receiptCheck?.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.getReceiptRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.receiptRows?.text = number.toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        salesImported = false
        productsImported = false
        paymentTypesImported = false
        saleTypesImported = false
        namesImported = false

        _binding = null
    }

    companion object {
        const val REQUEST_CODE_SALES = 1001
        const val REQUEST_CODE_PRODUCTS = 1002
        const val REQUEST_CODE_PAYMENT_TYPES = 1003
        const val REQUEST_CODE_SALE_TYPES = 1004
        const val REQUEST_CODE_NAMES = 1005
        const val REQUEST_CODE_RECEIPT = 1006
    }
}