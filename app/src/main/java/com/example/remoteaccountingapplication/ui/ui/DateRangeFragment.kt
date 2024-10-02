package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentDateRangeBinding
import com.example.remoteaccountingapplication.domain.Csv
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.ExportingCsvViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DateRangeFragment : Fragment() {

    private var _binding: FragmentDateRangeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExportingCsvViewModel by activityViewModels {
        ExportingCsvViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository
        )
    }

    private lateinit var csv: Csv

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

        csv = Csv(requireContext(), viewModel, this)

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
                csv.createAndShareCsv(startDateHeader, endDateHeader)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}