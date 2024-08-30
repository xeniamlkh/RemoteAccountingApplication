package com.example.remoteaccountingapplication.ui.alertdialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.remoteaccountingapplication.R

interface PermissionRationaleDialogListener {
    fun callPermissionLauncher()
}

class PermissionRationaleDialog(private val listener: PermissionRationaleDialogListener) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.rationale_message))
                .setTitle(getString(R.string.rationale_title))
                .setPositiveButton(getString(R.string.ok_btn)) { _, _ ->
                    listener.callPermissionLauncher()
                }
                .setNegativeButton(getString(R.string.decline_btn)) { _, _ ->

                }
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.activity_warning))
    }
}