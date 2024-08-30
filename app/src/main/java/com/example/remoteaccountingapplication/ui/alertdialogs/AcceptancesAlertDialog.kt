package com.example.remoteaccountingapplication.ui.alertdialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.remoteaccountingapplication.R

class AcceptancesAlertDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(getString(R.string.attention))
                .setMessage(getString(R.string.acceptance_message))
                .setPositiveButton(getString(R.string.ok_btn)) { _, _ ->
                    openAcceptancesNameFragment()
                }
                .setNegativeButton(getString(R.string.cancel_btn)) { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.activity_warning))
    }

    private fun openAcceptancesNameFragment() {
        findNavController()
            .navigate(
                AcceptancesAlertDialogDirections
                    .actionAcceptancesAlertDialogToAcceptanceNameFragment()
            )
    }
}