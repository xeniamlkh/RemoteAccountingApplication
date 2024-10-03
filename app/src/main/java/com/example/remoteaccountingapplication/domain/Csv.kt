package com.example.remoteaccountingapplication.domain

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class Csv {

    fun createCsvFile(csvTitle: String, errorMessage: String): File {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            throw IllegalStateException(errorMessage)
        }

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        return File(downloadDir, csvTitle)
    }

    fun writeCsvFile(
        exportedSales: List<String>,
        csvFile: File,
        header: String,
        charsetName: String
    ) {
        try {
            val stream = FileOutputStream(csvFile)
            val writer = OutputStreamWriter(stream, charsetName)

            writer.write(header)

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

}