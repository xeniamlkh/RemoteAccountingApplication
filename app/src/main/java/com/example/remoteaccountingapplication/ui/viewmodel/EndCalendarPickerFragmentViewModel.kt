package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import java.text.SimpleDateFormat
import java.util.Locale

class EndCalendarPickerFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    private val _endDateLivaData = MutableLiveData<String>()
    val endDateLiveData: LiveData<String> get() = _endDateLivaData

    private var endDateVM: Long? = null

    fun setEndDate(endDate: Long?) {
        if (endDate != null) {
            endDateVM = endDate

            val simpleCalendarDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendarDate: String = simpleCalendarDate.format(endDate)

            _endDateLivaData.value = calendarDate
        }
    }

}

class EndCalendarPickerFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EndCalendarPickerFragmentViewModel::class.java)) {
            return EndCalendarPickerFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}