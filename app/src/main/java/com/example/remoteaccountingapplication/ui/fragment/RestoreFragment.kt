package com.example.remoteaccountingapplication.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentRestoreBinding
import com.example.remoteaccountingapplication.domain.RestoreBackup
import com.example.remoteaccountingapplication.ui.viewmodel.RestoreFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RestoreFragmentViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestoreFragment : BaseFragment<FragmentRestoreBinding>() {

    private lateinit var restoreBackup: RestoreBackup
    private var currentRequestCode: Int = 0

    private val viewModel: RestoreFragmentViewModel by viewModels {
        RestoreFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRestoreBinding {
        return FragmentRestoreBinding.inflate(inflater, container, false)
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
                            else -> showSnackBar(getString(R.string.error_can_t_import_table))
                        }
                    }
                } else {
                    showSnackBar(getString(R.string.error_wrong_mime_type_choose_another_file))
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreBackup = RestoreBackup()

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

        viewModel.getReceiptRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.receiptRows.text = number.toString()
        }

        binding.restoreSalesBtn.setOnClickListener {
            currentRequestCode = REQUEST_CODE_SALES
            getContent()
        }

        binding.restoreProductsBtn.setOnClickListener {
            currentRequestCode = REQUEST_CODE_PRODUCTS
            getContent()
        }

        binding.restorePaymentTypesBtn.setOnClickListener {
            currentRequestCode = REQUEST_CODE_PAYMENT_TYPES
            getContent()
        }

        binding.restoreSaleTypesBtn.setOnClickListener {
            currentRequestCode = REQUEST_CODE_SALE_TYPES
            getContent()
        }

        binding.restoreNamesBtn.setOnClickListener {
            currentRequestCode = REQUEST_CODE_NAMES
            getContent()
        }

        binding.restoreReceiptBtn.setOnClickListener {
            currentRequestCode = REQUEST_CODE_RECEIPT
            getContent()
        }
    }

    private fun getContent() {
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
                    showSnackBar(getString(R.string.error_wrong_table_choose_a_sales_table))
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val salesList = restoreBackup.importSalesCsv(inputStream)

                    for (sale in salesList) {
                        viewModel.saveSale(sale)
                    }

                    showSnackBar(getString(R.string.sales_table_imported))

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
                    showSnackBar(getString(R.string.error_wrong_table_choose_a_products_table))
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val productsList = restoreBackup.importProductsCsv(inputStream)

                    for (productItem in productsList) {
                        viewModel.saveProductNote(productItem)
                    }

                    showSnackBar(getString(R.string.products_table_imported))

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
                if (!fileName.contains("PaymentTypes")) {
                    showSnackBar(getString(R.string.error_wrong_table_choose_a_payment_types_table))
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val paymentTypesList = restoreBackup.importPaymentTypesCsv(inputStream)

                    for (paymentType in paymentTypesList) {
                        viewModel.savePaymentType(paymentType)
                    }

                    showSnackBar(getString(R.string.payment_types_table_imported))

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
                if (!fileName.contains("SaleTypes")) {
                    showSnackBar(getString(R.string.error_wrong_table_choose_a_sale_types_table))
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val saleTypesList = restoreBackup.importSaleTypesCsv(inputStream)

                    for (saleType in saleTypesList) {
                        viewModel.saveSaleType(saleType)
                    }

                    showSnackBar(getString(R.string.sale_types_table_imported))

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
                    showSnackBar(getString(R.string.error_wrong_table_choose_a_names_table))
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val namesList = restoreBackup.importNamesCsv(inputStream)

                    for (name in namesList) {
                        viewModel.saveName(name)
                    }

                    showSnackBar(getString(R.string.names_table_imported))

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
                    showSnackBar(getString(R.string.error_wrong_table_choose_an_receipt_table))
                } else {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val receiptList = restoreBackup.importReceiptOfGoodsCsv(inputStream)

                    for (receipt in receiptList) {
                        viewModel.insertReceiptProduct(receipt)
                    }

                    showSnackBar(getString(R.string.receipt_table_imported))

                    requireActivity().runOnUiThread {
                        binding.receiptCheck.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.getReceiptRowsNumber().observe(this.viewLifecycleOwner) { number ->
            binding.receiptRows.text = number.toString()
        }

    }

    private fun showSnackBar(message: String) {
        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            )
            .show()
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