package com.example.remoteaccountingapplication.ui.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.remoteaccountingapplication.R
import com.example.remoteaccountingapplication.RemoteAccountingApplication
import com.example.remoteaccountingapplication.databinding.FragmentReportBinding
import com.example.remoteaccountingapplication.ui.alertdialogs.DeleteAlertDialog
import com.example.remoteaccountingapplication.ui.recyclerview.OnMenuClickListener
import com.example.remoteaccountingapplication.ui.recyclerview.SalesRecyclerViewAdapter
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModel
import com.example.remoteaccountingapplication.ui.viewmodel.RemoteAccountingViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportFragment : Fragment(), DateListener, OnMenuClickListener {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RemoteAccountingViewModel by activityViewModels {
        RemoteAccountingViewModelFactory(
            (activity?.application as RemoteAccountingApplication).repository,
            activity?.application as RemoteAccountingApplication
        )
    }

    private lateinit var adapter: SalesRecyclerViewAdapter

    private var currentDay: Int = 0
    private val simpleTodayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarInstance = Calendar.getInstance()
        binding.date.text = simpleTodayDate.format(calendarInstance.time)

        currentDay = calendarInstance.get(Calendar.DAY_OF_MONTH)
        val startOfToday = getStartOfTodayInMillis(currentDay)
        val endOfToday = getEndOfTodayInMillis(currentDay)

        getListOfSales(startOfToday, endOfToday)

        binding.previousDayBtn.setOnClickListener {
            currentDay -= 1

            val previousDayStart = getStartOfTodayInMillis(currentDay)
            val previousDayEnd = getEndOfTodayInMillis(currentDay)

            getListOfSales(previousDayStart, previousDayEnd)

        }

        binding.nextDayBtn.setOnClickListener {
            currentDay += 1

            val previousDayStart = getStartOfTodayInMillis(currentDay)
            val previousDayEnd = getEndOfTodayInMillis(currentDay)

            getListOfSales(previousDayStart, previousDayEnd)
        }

        binding.date.setOnClickListener {
            ReportCalendarPickerFragment(this).show(parentFragmentManager, "CALENDAR_PICKER")
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

    private fun getListOfSales(startOfToday: Long, endOfToday: Long) {
        viewModel.getListOfSalesByDate(startOfToday, endOfToday)
            .observe(this.viewLifecycleOwner) { salesList ->
                adapter = SalesRecyclerViewAdapter(salesList, this)
                binding.recyclerViewReport.adapter = adapter
            }

        viewModel.getTotalSum(startOfToday, endOfToday).observe(this.viewLifecycleOwner) { total ->
            if (total != null) {
                val argument = total.toString()
                binding.dayTotal.text = getString(R.string.total_text_double, argument)
            } else {
                binding.dayTotal.text = getString(R.string.total_text)
            }
        }
    }

    override fun setCalendarDate(dayOfMonth: Int) {
        currentDay = dayOfMonth

        val previousDayStart = getStartOfTodayInMillis(currentDay)
        val previousDayEnd = getEndOfTodayInMillis(currentDay)

        getListOfSales(previousDayStart, previousDayEnd)
    }

    override fun onMenuClick(menuView: View, itemId: Int) {
        val popupMenu = PopupMenu(requireContext(), menuView)
        popupMenu.inflate(R.menu.options_menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete_btn -> {
                        DeleteAlertDialog.newInstance(1, itemId)
                            .show(parentFragmentManager, "DELETE")
                        true
                    }

                    R.id.edit_btn -> {
                        view?.findNavController()?.navigate(
                            ReportFragmentDirections.actionReportFragmentToEditSaleFragment(itemId)
                        )
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        })
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}