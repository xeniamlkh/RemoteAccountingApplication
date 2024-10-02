package com.example.remoteaccountingapplication.ui.activity

import androidx.activity.viewModels
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteaccountingapplication.domain.Backup
import com.example.remoteaccountingapplication.domain.Csv
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.BackupViewModelFactory
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModelFactory

open class BaseActivity : AppCompatActivity() {

    private val backupViewModel: BackupViewModel by viewModels {
        BackupViewModelFactory((application as RemoteAccountingApplication).repository)
    }

    private val exportingCsvViewModel: ExportingCsvViewModel by viewModels {
        ExportingCsvViewModelFactory((application as RemoteAccountingApplication).repository)
    }

    private lateinit var backup: Backup
    private lateinit var csv: Csv

    fun createBackUp() {
        backup = Backup(backupViewModel, this)
        backup.createBackUp()
    }

    // sharing context?
    fun createAndShareCsv() {
        csv = Csv(this, exportingCsvViewModel, this)
        csv.createAndShareCsv(null, null)
    }
}