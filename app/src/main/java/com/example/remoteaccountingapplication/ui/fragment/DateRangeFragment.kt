package com.example.remoteaccountingapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentDateRangeBinding
import com.example.remoteaccountingapplication.domain.Csv
import com.example.remoteaccountingapplication.ui.shareCSVFile
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.io.File

class DateRangeFragment : BaseFragment<FragmentDateRangeBinding>() {

    private val viewModel: ExportingCsvViewModel by activityViewModels {
        ExportingCsvViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private lateinit var csv: Csv

    private var startDateHeader: String = ""
    private var endDateHeader: String = ""

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDateRangeBinding {
        return FragmentDateRangeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        csv = Csv()

        viewModel.startDateLiveData.observe(this.viewLifecycleOwner) { startDate ->
            binding.startDateText.text = getString(R.string.start_date_values, startDate)
            startDateHeader = startDate
        }

        viewModel.endDateLiveData.observe(this.viewLifecycleOwner) { endDate ->
            binding.endDateText.text = getString(R.string.end_date_values, endDate)
            endDateHeader = endDate
        }

        binding.startDateIcon.setOnClickListener {
            StartCalendarPickerFragment().show(
                parentFragmentManager,
                getString(R.string.calendar_picker)
            )
        }

        binding.endDateIcon.setOnClickListener {
            EndCalendarPickerFragment().show(
                parentFragmentManager,
                getString(R.string.calendar_picker)
            )
        }

        binding.exportBtn.setOnClickListener {

            if (!binding.startDateText.text.equals(getString(R.string.start_date)) &&
                !binding.endDateText.text.equals(getString(R.string.end_date))
            ) {
                createAndShareCsv(
                    getString(
                        R.string.export_csv_period,
                        startDateHeader,
                        endDateHeader
                    )
                )
                viewModel.clearRange()
            } else {
                Snackbar
                    .make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.fill_in_dates),
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }

    private fun createAndShareCsv(csvFileTitle: String) {
        val csvFile = createCsvReport(csvFileTitle)
        shareCsvReport(csvFile)
    }

    private fun createCsvReport(csvFileTitle: String): File {
        val csvHeader = getString(R.string.csv_header)
        val errorMessage = getString(R.string.external_storage_not_mounted)
        val charsetName = getString(R.string.utf_16)

        val csvFile = csv.createCsvFile(csvFileTitle, errorMessage)

        viewModel.exportRangeSalesCsv().observe(requireActivity()) { exportedSales ->
            csv.writeCsvFile(exportedSales, csvFile, csvHeader, charsetName)
        }

        return csvFile
    }

    private fun shareCsvReport(csvFile: File) {
        requireContext().shareCSVFile(csvFile)
    }
}