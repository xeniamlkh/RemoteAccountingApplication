package com.example.remoteaccountingapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.remoteaccountingapplication.databinding.ActivityMainBinding
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.remoteaccountingapplication.ui.alertdialogs.PermissionRationaleDialog
import com.example.remoteaccountingapplication.ui.alertdialogs.PermissionRationaleDialogListener
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.Exception

class MainActivity : AppCompatActivity(), PermissionRationaleDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var absolutPath: String

    private val viewModel: RemoteAccountingViewModel by viewModels {
        RemoteAccountingViewModelFactory(
            (application as RemoteAccountingApplication).repository,
            application as RemoteAccountingApplication
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->

            val writePermission =
                result.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)
            val readPermission =
                result.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false)

            if (writePermission && readPermission) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.permissions_granted), Snackbar.LENGTH_SHORT
                )
                    .show()

                createBackUp()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                createBackUp()
            }

            checkStoragePermissions()
        }

        setSupportActionBar(binding.toolbarView)

        navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
                    as NavHostFragment

        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    private fun checkStoragePermissions() {
        when {
            (ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat
                        .checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) -> {
                createBackUp()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                showExplanation()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showExplanation()
            }

            else -> {
                requestStoragePermissions()
            }

        }
    }

    private fun requestStoragePermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    private fun showExplanation() {
        PermissionRationaleDialog(this).show(supportFragmentManager, "RATIONALE")
    }

    override fun callPermissionLauncher() {
        requestStoragePermissions()
    }

    private fun createAndShareCsv() {
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reportFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.thirty_btn -> {
                createAndShareCsv()
                true
            }

            R.id.dateRangeFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.productsFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.paymentTypesFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.saleTypesFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.namesFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.backupFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.restoreFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.acceptancesAlertDialog -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.acceptanceReportFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createBackUp() {
        val simpleTodayDate = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val todayDate: String = simpleTodayDate.format(System.currentTimeMillis())

        checkBackupDir()
        countBackups()

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
}