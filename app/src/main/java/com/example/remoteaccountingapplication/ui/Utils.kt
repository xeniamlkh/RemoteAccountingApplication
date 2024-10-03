package com.example.remoteaccountingapplication.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.remoteaccountingapplication.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun Context.getTodayDateD(): String {
    val simpleTodayDate = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
    return simpleTodayDate.format(System.currentTimeMillis())
}

fun Context.getTodayDateY(): String {
    val simpleTodayDate = SimpleDateFormat(getString(R.string.yyyy_mm_dd), Locale.getDefault())
    return simpleTodayDate.format(System.currentTimeMillis())
}

fun Context.shareCSVFile(csvFile: File) {

    val fileUri: Uri = FileProvider
        .getUriForFile(this, getString(R.string.fileprovider), csvFile)

    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = getString(R.string.text_csv)
        putExtra(Intent.EXTRA_STREAM, fileUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    this.startActivity(Intent.createChooser(shareIntent, getString(R.string.send_csv)))
}