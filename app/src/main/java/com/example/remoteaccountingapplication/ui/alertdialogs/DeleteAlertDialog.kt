package com.example.remoteaccountingapplication.ui.alertdialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.ui.viewmodel.DeleteAlertDialogViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.DeleteAlertDialogViewModelFactory
import com.google.android.material.snackbar.Snackbar

private const val ARG_PARAM_TABLE = "table"
private const val ARG_PARAM_ID = "item_id"

class DeleteAlertDialog : DialogFragment() {

    private val viewModel: DeleteAlertDialogViewModel by viewModels {
        DeleteAlertDialogViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private var tableId: Int? = null
    private var itemId: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            tableId = it.getInt(ARG_PARAM_TABLE)
            itemId = it.getInt(ARG_PARAM_ID)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(getString(R.string.attention))
                .setMessage(getString(R.string.delete_message))
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    if (itemId != null) {
                        when (tableId) {
                            1 -> deleteSaleById(itemId!!)
                            2 -> deleteProductById(itemId!!)
                            3 -> deletePaymentTypeById(itemId!!)
                            4 -> deleteSaleTypeById(itemId!!)
                            5 -> deleteNameById(itemId!!)
                            else -> Snackbar
                                .make(
                                    requireActivity().findViewById(android.R.id.content),
                                    getString(R.string.no_item),
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                }
                .setNegativeButton(getString(R.string.cancel_btn)) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.activity_warning))
    }

    private fun deleteSaleById(itemId: Int) {
        viewModel.restoreProductRemains(itemId)
        viewModel.deleteSaleItemById(itemId)

        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.item_deleted_message),
                Snackbar.LENGTH_SHORT
            )
            .show()

    }

    private fun deleteProductById(itemId: Int) {
        viewModel.deleteProductById(itemId)

        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.item_deleted_message),
                Snackbar.LENGTH_SHORT
            )
            .show()

    }

    private fun deletePaymentTypeById(itemId: Int) {
        viewModel.deletePaymentTypeById(itemId)

        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.item_deleted_message),
                Snackbar.LENGTH_SHORT
            )
            .show()

    }

    private fun deleteSaleTypeById(itemId: Int) {
        viewModel.deleteSaleTypeById(itemId)

        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.item_deleted_message),
                Snackbar.LENGTH_SHORT
            )
            .show()

    }

    private fun deleteNameById(itemId: Int) {
        viewModel.deleteNameById(itemId)

        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.item_deleted_message),
                Snackbar.LENGTH_SHORT
            )
            .show()

    }

    companion object {
        fun newInstance(tableId: Int, itemId: Int) = DeleteAlertDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM_TABLE, tableId)
                putInt(ARG_PARAM_ID, itemId)
            }
        }
    }


}