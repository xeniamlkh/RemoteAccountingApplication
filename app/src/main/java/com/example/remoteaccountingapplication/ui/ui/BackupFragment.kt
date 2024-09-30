package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentBackupBinding
import com.example.remoteaccountingapplication.ui.viewmodel.BackupFragmentModelFactory
import com.example.remoteaccountingapplication.ui.viewmodel.BackupFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale

class BackupFragment : Fragment() {

    private var _binding: FragmentBackupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BackupFragmentViewModel by viewModels {
        BackupFragmentModelFactory((activity?.application as RemoteAccountingApplication).repository)
    }

    private lateinit var absolutPath: String

    private val simpleTodayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val todayDate: String = simpleTodayDate.format(System.currentTimeMillis())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkBackupDir()

        binding.backupBtn.setOnClickListener {

            countBackups()

            val filePath = String.format(
                "%1s/%2s",
                absolutPath,
                todayDate
            )

            createSalesBackup(filePath)
            createProductsBackUp(filePath)
            createPaymentTypesBackUp(filePath)
            createSaleTypesBackUp(filePath)
            createNamesBackUp(filePath)
            createReceiptOfGoodsBackUp(filePath)

            Snackbar
                .make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.db_backup_created),
                    Snackbar.LENGTH_SHORT
                )
                .show()
        }
    }

    private fun createSalesBackup(filePath: String) {

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        val csvFile = File(fileDir, getString(R.string.sales_csv_backup_title, todayDate))

        viewModel.dailyAllSalesBackUp().observe(this.viewLifecycleOwner) { allSales ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (salesLine in allSales) {
                    writer.write(salesLine)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.salesCheck.visibility = View.VISIBLE
    }

    private fun createProductsBackUp(filePath: String) {

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        val csvFile = File(fileDir, getString(R.string.products_csv_backup_title, todayDate))

        viewModel.dailyProductsBackUp().observe(this.viewLifecycleOwner) { products ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (product in products) {
                    writer.write(product)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.productsCheck.visibility = View.VISIBLE
    }

    private fun createPaymentTypesBackUp(filePath: String) {

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        val csvFile = File(fileDir, getString(R.string.payment_types_csv_backup_title, todayDate))

        viewModel.dailyPaymentTypeBackUp().observe(this.viewLifecycleOwner) { paymentTypes ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (paymentType in paymentTypes) {
                    writer.write(paymentType)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.paymentTypesCheck.visibility = View.VISIBLE
    }

    private fun createSaleTypesBackUp(filePath: String) {

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        val csvFile = File(fileDir, getString(R.string.sale_types_csv_backup_title, todayDate))

        viewModel.dailySaleTypeBackUp().observe(this.viewLifecycleOwner) { saleTypes ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (saleType in saleTypes) {
                    writer.write(saleType)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.saleTypesCheck.visibility = View.VISIBLE
    }

    private fun createNamesBackUp(filePath: String) {

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        val csvFile = File(fileDir, getString(R.string.names_csv_backup_title, todayDate))

        viewModel.dailyNamesBackUp().observe(this.viewLifecycleOwner) { names ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (name in names) {
                    writer.write(name)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.namesCheck.visibility = View.VISIBLE
    }

    private fun createReceiptOfGoodsBackUp(filePath: String) {

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()
        }

        val csvFile = File(fileDir, getString(R.string.receipt_csv_backup_title, todayDate))

        viewModel.dailyReceiptBackUpFlow().observe(this.viewLifecycleOwner) { receipt ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (item in receipt) {
                    writer.write(item)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.receiptCheck?.visibility = View.VISIBLE
    }

    private fun checkBackupDir() {

        val docFolder = File("${Environment.getExternalStorageDirectory()}/Documents")

        if (!docFolder.exists()) {

            docFolder.mkdir()

            val backupFolder = File(
                "${
                    Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                }/RemoteAccountingBackup"
            )
            backupFolder.mkdir()

            absolutPath = String.format(
                "%1s/%2s",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "RemoteAccountingBackup"
            )
        } else {
            val backupFolder = File(
                "${
                    Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                }/RemoteAccountingBackup"
            )

            if (!backupFolder.exists()) {
                backupFolder.mkdir()
            }

            absolutPath = String.format(
                "%1s/%2s",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "RemoteAccountingBackup"
            )
        }
    }

    private fun countBackups() {
        val backupFolder = File(
            "${
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            }/RemoteAccountingBackup"
        )

        if (backupFolder.exists() && backupFolder.isDirectory) {
            val subFolders =
                backupFolder.listFiles { file -> file.isDirectory }?.toList() ?: emptyList()

            if (subFolders.size > 5) {
                val sortedSubFolders = subFolders.sortedBy { it.lastModified() }
                val oldestFolder = sortedSubFolders.first()
                deleteRecursively(oldestFolder)
            }
        }
    }

    private fun deleteRecursively(file: File): Boolean {
        if (file.isDirectory) {
            val children = file.listFiles() ?: return false
            for (child in children) {
                if (!deleteRecursively(child)) {
                    return false
                }
            }
        }
        return file.delete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}