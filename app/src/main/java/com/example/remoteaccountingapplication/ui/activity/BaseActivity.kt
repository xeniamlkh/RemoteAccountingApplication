package com.example.remoteaccountingapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.remoteaccountingapplication.domain.Backup
import com.example.remoteaccountingapplication.ui.viewmodel.ActivityViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ActivityViewModelFactory

open class BaseActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels {
        ActivityViewModelFactory((application as RemoteAccountingApplication).repository)
    }

    private lateinit var backup: Backup

    fun createBackUp(){
        backup = Backup(viewModel, this)
        backup.createBackUp()
    }

    //------
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