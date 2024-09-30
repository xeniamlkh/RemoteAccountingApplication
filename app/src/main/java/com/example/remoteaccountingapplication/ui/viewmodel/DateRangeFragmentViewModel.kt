package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository

class DateRangeFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    private val _startDateLivaData = MutableLiveData<String>()
    val startDateLiveData: LiveData<String> get() = _startDateLivaData

    private val _endDateLivaData = MutableLiveData<String>()
    val endDateLiveData: LiveData<String> get() = _endDateLivaData

    private var startDateVM: Long? = null // move up?
    private var endDateVM: Long? = null // move up?

    fun exportRangeSalesCsv(): LiveData<List<String>> {
        return repository.exportRangeSalesCsv(startDateVM!!, endDateVM!!).asLiveData() // null
    }

    fun clearRange() {
        startDateVM = null
        endDateVM = null

        _startDateLivaData.value = ""
        _endDateLivaData.value = ""
    }
}

class DateRangeFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DateRangeFragmentViewModel::class.java)) {
            return DateRangeFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}