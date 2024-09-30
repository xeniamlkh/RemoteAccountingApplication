package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import java.text.SimpleDateFormat
import java.util.Locale

class StartCalendarPickerFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    private val _startDateLivaData = MutableLiveData<String>()
    val startDateLiveData: LiveData<String> get() = _startDateLivaData

    private var startDateVM: Long? = null

    fun setStartDate(startDate: Long?) {
        if (startDate != null) {
            startDateVM = startDate

            val simpleCalendarDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendarDate: String = simpleCalendarDate.format(startDate)

            _startDateLivaData.value = calendarDate
        }
    }

}

class StartCalendarPickerFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(StartCalendarPickerFragmentViewModel::class.java)) {
            return StartCalendarPickerFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}