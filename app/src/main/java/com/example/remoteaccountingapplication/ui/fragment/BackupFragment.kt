package com.example.remoteaccountingapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentBackupBinding
import com.example.remoteaccountingapplication.domain.Backup
import com.example.remoteaccountingapplication.ui.getTodayDateY
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModelFactory
import com.google.android.material.snackbar.Snackbar

class BackupFragment : BaseFragment<FragmentBackupBinding>() {

    private val viewModel: BackupViewModel by viewModels {
        BackupViewModelFactory((activity?.application as RemoteAccountingApplication).repository)
    }

    private lateinit var backup: Backup

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBackupBinding {
        return FragmentBackupBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backup = Backup()

        binding.backupBtn.setOnClickListener {
            val todayDateY = requireContext().getTodayDateY()
            val todayBackupDir = backup.createTodayBackupDir(todayDateY)

            val csvSalesTitle = getString(R.string.sales_csv_backup_title, todayDateY)
            val csvSalesFile = backup.createTodayBackupFile(todayBackupDir, csvSalesTitle)
            viewModel.dailyAllSalesBackUp().observe(requireActivity()) { allSales ->
                backup.writeBackup(allSales, csvSalesFile)
            }
            binding.salesCheck.visibility = View.VISIBLE

            val csvProductsTitle = getString(R.string.products_csv_backup_title, todayDateY)
            val csvProductsFile = backup.createTodayBackupFile(todayBackupDir, csvProductsTitle)
            viewModel.dailyProductsBackUp().observe(requireActivity()) { allProducts ->
                backup.writeBackup(allProducts, csvProductsFile)
            }
            binding.productsCheck.visibility = View.VISIBLE

            val csvPaymentTypesTitle =
                getString(R.string.payment_types_csv_backup_title, todayDateY)
            val csvPaymentTypesFile =
                backup.createTodayBackupFile(todayBackupDir, csvPaymentTypesTitle)
            viewModel.dailyPaymentTypeBackUp().observe(requireActivity()) { allPaymentTypes ->
                backup.writeBackup(allPaymentTypes, csvPaymentTypesFile)
            }
            binding.paymentTypesCheck.visibility = View.VISIBLE

            val csvSaleTypesTitle = getString(R.string.sale_types_csv_backup_title, todayDateY)
            val csvSaleTypesFile = backup.createTodayBackupFile(todayBackupDir, csvSaleTypesTitle)
            viewModel.dailySaleTypeBackUp().observe(requireActivity()) { allSaleTypes ->
                backup.writeBackup(allSaleTypes, csvSaleTypesFile)
            }
            binding.saleTypesCheck.visibility = View.VISIBLE

            val csvNamesTitle = getString(R.string.names_csv_backup_title, todayDateY)
            val csvNamesFile = backup.createTodayBackupFile(todayBackupDir, csvNamesTitle)
            viewModel.dailyNamesBackUp().observe(requireActivity()) { allNames ->
                backup.writeBackup(allNames, csvNamesFile)
            }
            binding.namesCheck.visibility = View.VISIBLE

            val csvReceiptTitle = getString(R.string.receipt_csv_backup_title, todayDateY)
            val csvReceiptFile = backup.createTodayBackupFile(todayBackupDir, csvReceiptTitle)
            viewModel.dailyReceiptBackUp().observe(requireActivity()) { allReceipt ->
                backup.writeBackup(allReceipt, csvReceiptFile)
            }
            binding.receiptCheck.visibility = View.VISIBLE

            Snackbar
                .make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.db_backup_created),
                    Snackbar.LENGTH_SHORT
                )
                .show()
        }
    }
}