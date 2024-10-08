package com.example.remoteaccountingapplication.ui.activity

import androidx.activity.viewModels
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.domain.Backup
import com.example.remoteaccountingapplication.domain.Csv
import com.example.remoteaccountingapplication.ui.getTodayDateY
import com.example.remoteaccountingapplication.ui.shareCSVFile
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModelFactory
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModelFactory
import java.io.File

open class BaseActivity : AppCompatActivity() {

    private val backupViewModel: BackupViewModel by viewModels {
        BackupViewModelFactory((application as RemoteAccountingApplication).repository)
    }

    private val exportingCsvViewModel: ExportingCsvViewModel by viewModels {
        ExportingCsvViewModelFactory((application as RemoteAccountingApplication).repository)
    }

    private val backup: Backup = Backup()
    private lateinit var csv: Csv

    fun createBackUp() {
        val todayDateY = this.getTodayDateY()
        val todayBackupDir = backup.createTodayBackupFolder(todayDateY)

        val csvSalesTitle = getString(R.string.sales_csv_backup_title, todayDateY)
        val csvSalesFile = backup.createTodayBackupFile(todayBackupDir, csvSalesTitle)
        if (csvSalesFile.length() == 0L) {
            backupViewModel.dailyAllSalesBackUp().observe(this) { allSales ->
                backup.writeBackup(allSales, csvSalesFile)
            }
        }

        val csvProductsTitle = getString(R.string.products_csv_backup_title, todayDateY)
        val csvProductsFile = backup.createTodayBackupFile(todayBackupDir, csvProductsTitle)
        if (csvProductsFile.length() == 0L) {
            backupViewModel.dailyProductsBackUp().observe(this) { allProducts ->
                backup.writeBackup(allProducts, csvProductsFile)
            }
        }


        val csvPaymentTypesTitle = getString(R.string.payment_types_csv_backup_title, todayDateY)
        val csvPaymentTypesFile = backup.createTodayBackupFile(todayBackupDir, csvPaymentTypesTitle)
        if (csvPaymentTypesFile.length() == 0L) {
            backupViewModel.dailyPaymentTypeBackUp().observe(this) { allPaymentTypes ->
                backup.writeBackup(allPaymentTypes, csvPaymentTypesFile)
            }
        }

        val csvSaleTypesTitle = getString(R.string.sale_types_csv_backup_title, todayDateY)
        val csvSaleTypesFile = backup.createTodayBackupFile(todayBackupDir, csvSaleTypesTitle)
        if (csvSaleTypesFile.length() == 0L) {
            backupViewModel.dailySaleTypeBackUp().observe(this) { allSaleTypes ->
                backup.writeBackup(allSaleTypes, csvSaleTypesFile)
            }
        }

        val csvNamesTitle = getString(R.string.names_csv_backup_title, todayDateY)
        val csvNamesFile = backup.createTodayBackupFile(todayBackupDir, csvNamesTitle)
        if (csvNamesFile.length() == 0L) {
            backupViewModel.dailyNamesBackUp().observe(this) { allNames ->
                backup.writeBackup(allNames, csvNamesFile)
            }
        }

        val csvReceiptTitle = getString(R.string.receipt_csv_backup_title, todayDateY)
        val csvReceiptFile = backup.createTodayBackupFile(todayBackupDir, csvReceiptTitle)
        if (csvReceiptFile.length() == 0L) {
            backupViewModel.dailyReceiptBackUp().observe(this) { allReceipt ->
                backup.writeBackup(allReceipt, csvReceiptFile)
            }
        }
    }

    fun createAndShareCsv(csvFileTitle: String) {
        val csvFile = createCsvReport(csvFileTitle)
        shareCsvReport(csvFile)
    }

    private fun createCsvReport(csvFileTitle: String): File {
        csv = Csv()

        val csvHeader = getString(R.string.csv_header)
        val errorMessage = getString(R.string.external_storage_not_mounted)
        val charsetName = getString(R.string.utf_16)

        val csvFile = csv.createCsvFile(csvFileTitle, errorMessage)

        exportingCsvViewModel.exportMonthSales().observe(this) { exportedSales ->
            csv.writeCsvFile(exportedSales, csvFile, csvHeader, charsetName)
        }

        return csvFile
    }

    private fun shareCsvReport(csvFile: File) {
        this.shareCSVFile(csvFile)
    }
}