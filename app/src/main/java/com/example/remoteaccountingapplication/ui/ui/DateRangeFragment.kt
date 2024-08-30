package com.example.remoteaccountingapplication.ui.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentDateRangeBinding
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class DateRangeFragment : Fragment() {

    private var _binding: FragmentDateRangeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RemoteAccountingViewModel by activityViewModels {
        RemoteAccountingViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository,
            activity?.application as RemoteAccountingApplication
        )
    }

    private var startDateHeader: String = ""
    private var endDateHeader: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateRangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startDateLiveData.observe(this.viewLifecycleOwner) { startDate ->
            binding.startDateText.text = getString(R.string.start_date_values, startDate)
            startDateHeader = startDate
        }

        viewModel.endDateLiveData.observe(this.viewLifecycleOwner) { endDate ->
            binding.endDateText.text = getString(R.string.end_date_values, endDate)
            endDateHeader = endDate
        }

        binding.startDateIcon.setOnClickListener {
            StartCalendarPickerFragment().show(parentFragmentManager, "CALENDAR_PICKER")
        }

        binding.endDateIcon.setOnClickListener {
            EndCalendarPickerFragment().show(parentFragmentManager, "CALENDAR_PICKER")
        }

        binding.exportBtn.setOnClickListener {

            if (!binding.startDateText.text.equals(getString(R.string.start_date)) &&
                !binding.endDateText.text.equals(getString(R.string.end_date))
            ) {
                val csvFile = createCsvFile()
                shareCSVFile(requireContext(), csvFile)

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

    private fun createCsvFile(): File {

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            throw IllegalStateException(getString(R.string.external_storage_not_mounted))
        }

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val csvFile =
            File(downloadDir, getString(R.string.export_csv_period, startDateHeader, endDateHeader))

        viewModel.exportRangeSalesCsv().observe(this.viewLifecycleOwner) { exportedSales ->

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}