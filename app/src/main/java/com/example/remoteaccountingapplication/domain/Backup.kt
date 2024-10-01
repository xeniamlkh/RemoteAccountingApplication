package com.example.remoteaccountingapplication.domain

import android.os.Environment
import androidx.lifecycle.LifecycleOwner
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale

class Backup(private val viewModel: BackupViewModel, private val lifecycleOwner: LifecycleOwner) {

    private lateinit var absolutPath: String
    private val simpleTodayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val todayDate: String = simpleTodayDate.format(System.currentTimeMillis())

    fun createBackUp() {
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
        }

        createSalesBackup(fileDir)
        createProductsBackUp(fileDir)
        createPaymentTypesBackUp(fileDir)
        createSaleTypesBackUp(fileDir)
        createNamesBackUp(fileDir)
        createReceiptOfGoodsBackUp(fileDir)

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

    private fun createSalesBackup(fileDir: File) {
        //getString(R.string.sales_csv_backup_title, todayDate) ???
        val csvFile = File(fileDir, "$todayDate Sales.csv")

        viewModel.dailyAllSalesBackUp().observe(lifecycleOwner) { allSales ->

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

    private fun createProductsBackUp(fileDir: File) {
        //Resources.getSystem().getString(R.string.products_csv_backup_title, todayDate)
        val csvFile = File(fileDir, "$todayDate Products.csv")

        viewModel.dailyProductsBackUp().observe(lifecycleOwner) { products ->

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

    private fun createPaymentTypesBackUp(fileDir: File) {
        val csvFile = File(fileDir, "$todayDate PaymentTypes.csv")

        viewModel.dailyPaymentTypeBackUp().observe(lifecycleOwner) { paymentTypes ->

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
    }

    private fun createSaleTypesBackUp(fileDir: File) {
        val csvFile = File(fileDir, "$todayDate SaleTypes.csv")

        viewModel.dailySaleTypeBackUp().observe(lifecycleOwner) { saleTypes ->

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

    private fun createNamesBackUp(fileDir: File) {
        val csvFile = File(fileDir, "$todayDate Names.csv")

        viewModel.dailyNamesBackUp().observe(lifecycleOwner) { names ->

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
    }

    private fun createReceiptOfGoodsBackUp(fileDir: File) {
        val csvFile = File(fileDir, "$todayDate ReceiptOfGoods.csv")

        viewModel.dailyReceiptBackUpFlow().observe(lifecycleOwner) { receipt ->

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
    }
}