package com.example.remoteaccountingapplication.ui.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentReceiptReportBinding
import com.example.remoteaccountingapplication.ui.recyclerview.ReceiptRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReceiptReportFragment : Fragment(), DateListener {

    private var _binding: FragmentReceiptReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RemoteAccountingViewModel by activityViewModels {
        RemoteAccountingViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository,
            activity?.application as RemoteAccountingApplication
        )
    }

    private lateinit var adapter: ReceiptRecyclerViewAdapter

    private var currentDay: Int = 0
    private val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReceiptReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarInstance = Calendar.getInstance()
        binding.date.text = simpleTodayDate.format(calendarInstance.time)

        currentDay = calendarInstance.get(Calendar.DAY_OF_MONTH)
        val startOfToday = getStartOfTodayInMillis(currentDay)
        val endOfToday = getEndOfTodayInMillis(currentDay)

        getListOfReceiptOfGoods(startOfToday, endOfToday)

        binding.previousDayBtn.setOnClickListener {
            currentDay -= 1

            val previousDayStart = getStartOfTodayInMillis(currentDay)
            val previousDayEnd = getEndOfTodayInMillis(currentDay)

            getListOfReceiptOfGoods(previousDayStart, previousDayEnd)
        }

        binding.nextDayBtn.setOnClickListener {
            currentDay += 1

            val previousDayStart = getStartOfTodayInMillis(currentDay)
            val previousDayEnd = getEndOfTodayInMillis(currentDay)

            getListOfReceiptOfGoods(previousDayStart, previousDayEnd)
        }

        binding.date.setOnClickListener {
            ReportCalendarPickerFragment(this)
                .show(parentFragmentManager, "CALENDAR_PICKER")
        }
    }

    private fun getStartOfTodayInMillis(day: Int): Long {
        val calendarInstance = Calendar.getInstance()
        calendarInstance.set(Calendar.DAY_OF_MONTH, day)
        calendarInstance.set(Calendar.HOUR_OF_DAY, 0)
        calendarInstance.set(Calendar.MINUTE, 0)
        calendarInstance.set(Calendar.SECOND, 0)
        calendarInstance.set(Calendar.MILLISECOND, 0)
        binding.date.text = simpleTodayDate.format(calendarInstance.time)
        return calendarInstance.timeInMillis
    }

    private fun getEndOfTodayInMillis(day: Int): Long {
        val calendarInstance = Calendar.getInstance()
        calendarInstance.set(Calendar.DAY_OF_MONTH, day)
        calendarInstance.set(Calendar.HOUR_OF_DAY, 23)
        calendarInstance.set(Calendar.MINUTE, 59)
        calendarInstance.set(Calendar.SECOND, 59)
        calendarInstance.set(Calendar.MILLISECOND, 999)
        return calendarInstance.timeInMillis
    }

    private fun getListOfReceiptOfGoods(startOfToday: Long, endOfToday: Long) {

        viewModel.getListOfReceiptByDate(startOfToday, endOfToday)
            .observe(this.viewLifecycleOwner) { receiptList ->
                adapter = ReceiptRecyclerViewAdapter(receiptList)
                binding.recyclerViewReport.adapter = adapter
            }
    }

    override fun setCalendarDate(dayOfMonth: Int) {
        currentDay = dayOfMonth

        val previousDayStart = getStartOfTodayInMillis(currentDay)
        val previousDayEnd = getEndOfTodayInMillis(currentDay)

        getListOfReceiptOfGoods(previousDayStart, previousDayEnd)
    }
}