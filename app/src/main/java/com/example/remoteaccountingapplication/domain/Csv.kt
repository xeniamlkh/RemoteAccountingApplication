package com.example.remoteaccountingapplication.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale

class Csv(
    private val context: Context,
    private val viewModel: ExportingCsvViewModel,
    private val lifecycleOwner: LifecycleOwner
) {

    fun createAndShareCsv(startDateHeader: String?, endDateHeader: String?) {
        if (startDateHeader.isNullOrEmpty() && endDateHeader.isNullOrEmpty()) {
            val csvFile = createCsvFileMonth()
            shareCSVFile(context, csvFile)
        } else {
            val csvFile = createCsvFileRange(startDateHeader, endDateHeader)
            shareCSVFile(context, csvFile)
        }
    }

    private fun createCsvFileMonth(): File {
        val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val todayDate: String = simpleTodayDate.format(System.currentTimeMillis())

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            throw IllegalStateException("External storage not mounted")
        }

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        //getString(R.string.csv_title_date, todayDate)
        val csvFile = File(downloadDir, "Выгрузка от $todayDate.csv")

        viewModel.exportMonthSales().observe(lifecycleOwner) { exportedSales ->
            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                //getString(R.string.csv_title)
                writer
                    .write("Дата,Товар,Цена,Количество,Сумма,Тип оплаты,Тип продажи,ФИО,Примечание")
                writer.write("\n")

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

    private fun createCsvFileRange(startDateHeader: String?, endDateHeader: String?): File {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            throw IllegalStateException("External storage not mounted")
        }

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val csvFile =
            File(downloadDir, "Выгрузка за период с $startDateHeader по $endDateHeader.csv")

        viewModel.exportRangeSalesCsv().observe(lifecycleOwner) { exportedSales ->

            try {
                val stream = FileOutputStream(csvFile)
                val writer = OutputStreamWriter(stream, "UTF-16")

                writer
                    .write("Дата,Товар,Цена,Количество,Сумма,Тип оплаты,Тип продажи,ФИО,Примечание")
                writer.write("\n")

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
            .getUriForFile(
                context, "com.example.remoteaccountingapplication.fileprovider", csvFile
            )

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        //getString(R.string.send_csv)
        context.startActivity(Intent.createChooser(shareIntent, "Переслать CSV"))
    }
}