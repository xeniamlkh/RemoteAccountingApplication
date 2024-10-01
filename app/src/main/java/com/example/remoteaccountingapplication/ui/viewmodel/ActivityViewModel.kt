package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import java.util.Calendar

class ActivityViewModel(private val repository: RoomRepository) : BackupViewModel(repository) {

    fun exportMonthSales(): LiveData<List<String>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -30)
        val startDate = calendar.timeInMillis

        return repository.exportMonthSalesCsv(startDate).asLiveData()
    }
}

class ActivityViewModelFactory(private val repository: RoomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            return ActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}