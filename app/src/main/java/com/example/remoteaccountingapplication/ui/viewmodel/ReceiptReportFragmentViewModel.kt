package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Receipt

class ReceiptReportFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getListOfReceiptByDate(startOfDay: Long, endOfDay: Long): LiveData<List<Receipt>> {
        return repository.getListOfReceiptByDate(startOfDay, endOfDay).asLiveData()
    }

}

class ReceiptReportFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ReceiptReportFragmentViewModel::class.java)) {
            return ReceiptReportFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}