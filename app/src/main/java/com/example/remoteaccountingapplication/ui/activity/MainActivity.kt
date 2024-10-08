package com.example.remoteaccountingapplication.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.remoteaccountingapplication.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.ui.alertdialogs.PermissionRationaleDialog
import com.example.remoteaccountingapplication.ui.alertdialogs.PermissionRationaleDialogListener
import com.example.remoteaccountingapplication.ui.getTodayDateD
import com.google.android.material.snackbar.Snackbar

class MainActivity : BaseActivity(), PermissionRationaleDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

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
        PermissionRationaleDialog(this).show(
            supportFragmentManager,
            getString(R.string.rationale_tag)
        )
    }

    override fun callPermissionLauncher() {
        requestStoragePermissions()
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
                createAndShareCsv(getString(R.string.csv_title_date, this.getTodayDateD()))
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

            R.id.receiptAlertDialog -> {
                item.onNavDestinationSelected(navController)
                true
            }

            R.id.receiptReportFragment -> {
                item.onNavDestinationSelected(navController)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}