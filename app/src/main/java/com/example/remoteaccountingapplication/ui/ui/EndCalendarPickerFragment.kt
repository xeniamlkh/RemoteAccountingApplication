package com.example.remoteaccountingapplication.ui.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.ui.viewmodel.EndCalendarPickerFragmentViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.EndCalendarPickerFragmentViewModelFactory

class EndCalendarPickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel: EndCalendarPickerFragmentViewModel by viewModels {
        EndCalendarPickerFragmentViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            this,
            year,
            month,
            day
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        val dateMilliseconds = date.timeInMillis

        viewModel.setEndDate(dateMilliseconds)
    }

}