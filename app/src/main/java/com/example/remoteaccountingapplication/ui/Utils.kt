package com.example.remoteaccountingapplication.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun Context.getTodayDateD(): String {
    val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return simpleTodayDate.format(System.currentTimeMillis())
}

fun Context.getTodayDateY(): String {
    val simpleTodayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return simpleTodayDate.format(System.currentTimeMillis())
}

fun Context.shareCSVFile(csvFile: File) {

    val fileUri: Uri = FileProvider
        .getUriForFile(
            this, "com.example.remoteaccountingapplication.fileprovider", csvFile
        )

    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, fileUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    //getString(R.string.send_csv)
    this.startActivity(Intent.createChooser(shareIntent, "Переслать CSV"))
}