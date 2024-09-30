package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Sales

class ReportFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getListOfSalesByDate(startOfDay: Long, endOfDay: Long): LiveData<List<Sales>> {
        return repository.getListOfSalesByDate(startOfDay, endOfDay).asLiveData()
    }

    fun getTotalSum(startOfDay: Long, endOfDay: Long): LiveData<Double> {
        return repository.getTotalSum(startOfDay, endOfDay).asLiveData()
    }
}

class ReportFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ReportFragmentViewModel::class.java)) {
            return ReportFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}