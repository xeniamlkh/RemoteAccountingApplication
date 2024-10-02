package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExportingCsvViewModel(private val repository: RoomRepository) : ViewModel() {

    private val _startDateLivaData = MutableLiveData<String>()
    val startDateLiveData: LiveData<String> get() = _startDateLivaData

    private val _endDateLivaData = MutableLiveData<String>()
    val endDateLiveData: LiveData<String> get() = _endDateLivaData

    private var startDateVM: Long? = null
    private var endDateVM: Long? = null

    fun setStartDate(startDate: Long?) {
        if (startDate != null) {
            startDateVM = startDate

            val simpleCalendarDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendarDate: String = simpleCalendarDate.format(startDate)

            _startDateLivaData.value = calendarDate
        }
    }

    fun setEndDate(endDate: Long?) {
        if (endDate != null) {
            endDateVM = endDate

            val simpleCalendarDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendarDate: String = simpleCalendarDate.format(endDate)

            _endDateLivaData.value = calendarDate
        }
    }

    fun exportRangeSalesCsv(): LiveData<List<String>> {
        return repository.exportRangeSalesCsv(startDateVM!!, endDateVM!!).asLiveData() // null
    }

    fun exportMonthSales(): LiveData<List<String>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -30)
        val startDate = calendar.timeInMillis

        return repository.exportMonthSalesCsv(startDate).asLiveData()
    }

    fun clearRange() {
        startDateVM = null
        endDateVM = null

        _startDateLivaData.value = ""
        _endDateLivaData.value = ""
    }
}

class ExportingCsvViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ExportingCsvViewModel::class.java)) {
            return ExportingCsvViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}