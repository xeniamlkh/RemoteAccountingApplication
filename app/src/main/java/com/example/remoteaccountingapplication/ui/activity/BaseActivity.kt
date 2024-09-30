package com.example.remoteaccountingapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.remoteaccountingapplication.ui.viewmodel.ActivityViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ActivityViewModelFactory

open class BaseActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels {
        ActivityViewModelFactory((application as RemoteAccountingApplication).repository)
    }

    private lateinit var absolutPath: String

    fun createBackUp() {
        val simpleTodayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val todayDate: String = simpleTodayDate.format(System.currentTimeMillis())

        checkBackupDir()
        countBackups()

        // initialize absolut path here as the return from checkBackupDir()?
        val filePath = String.format(
            "%1s/%2s",
            absolutPath,
            todayDate
        )

        val fileDir = File(filePath)

        if (!fileDir.exists()) {
            fileDir.mkdir()

            createSalesBackup(fileDir, todayDate)
            createProductsBackUp(fileDir, todayDate)
            createPaymentTypesBackUp(fileDir, todayDate)
            createSaleTypesBackUp(fileDir, todayDate)
            createNamesBackUp(fileDir, todayDate)
            createAcceptancesBackUp(fileDir, todayDate)

        }
    }

    private fun checkBackupDir() { //return absolut path as a result?
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

    private fun createSalesBackup(fileDir: File, todayDate: String) {
        val csvFile = File(fileDir, getString(R.string.sales_csv_backup_title, todayDate))

        viewModel.dailyAllSalesBackUp().observe(this) { allSales ->
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


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createProductsBackUp(fileDir: File, todayDate: String) {
        val csvFile = File(fileDir, getString(R.string.products_csv_backup_title, todayDate))

        viewModel.dailyProductsBackUp().observe(this) { products ->

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

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createPaymentTypesBackUp(fileDir: File, todayDate: String) {
        val csvFile = File(fileDir, getString(R.string.payment_types_csv_backup_title, todayDate))

        viewModel.dailyPaymentTypeBackUp().observe(this) { paymentTypes ->

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

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createSaleTypesBackUp(fileDir: File, todayDate: String) {
        val csvFile = File(fileDir, getString(R.string.sale_types_csv_backup_title, todayDate))

        viewModel.dailySaleTypeBackUp().observe(this) { saleTypes ->

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

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createNamesBackUp(fileDir: File, todayDate: String) {
        val csvFile = File(fileDir, getString(R.string.names_csv_backup_title, todayDate))

        viewModel.dailyNamesBackUp().observe(this) { names ->

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

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createAcceptancesBackUp(fileDir: File, todayDate: String) {
        val csvFile = File(fileDir, getString(R.string.acceptances_csv_backup_title, todayDate))

        viewModel.dailyAcceptancesBackUpFlow().observe(this) { acceptances ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                for (acceptance in acceptances) {
                    writer.write(acceptance)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createAndShareCsv() {
        val csvFile = createCsvFile()
        shareCSVFile(this, csvFile)
    }

    private fun createCsvFile(): File {
        val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val todayDate: String = simpleTodayDate.format(System.currentTimeMillis())

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            throw IllegalStateException("External storage not mounted")
        }

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val csvFile = File(downloadDir, getString(R.string.csv_title_date, todayDate))

        viewModel.exportMonthSales().observe(this) { exportedSales ->
            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                writer.write(getString(R.string.csv_title))

                for (salesLine in exportedSales) {
                    writer.write(salesLine)
                    writer.write("\n")
                }

                writer.flush()
                writer.close()
                stream.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return csvFile
    }

    private fun shareCSVFile(context: Context, csvFile: File) {
        val fileUri: Uri = FileProvider
            .getUriForFile(context, "com.example.remoteaccountingapplication.fileprovider", csvFile)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, getString(R.string.send_csv)))
    }
}