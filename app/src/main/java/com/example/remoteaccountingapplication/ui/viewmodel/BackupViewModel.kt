package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository

open class BackupViewModel(private val repository: RoomRepository): ViewModel()  {

    fun dailyAllSalesBackUp(): LiveData<List<String>> {
        return repository.dailyAllSalesBackUp().asLiveData()
    }

    fun dailyProductsBackUp(): LiveData<List<String>> {
        return repository.dailyProductsBackUp().asLiveData()
    }

    fun dailyPaymentTypeBackUp(): LiveData<List<String>> {
        return repository.dailyPaymentTypeBackUp().asLiveData()
    }

    fun dailySaleTypeBackUp(): LiveData<List<String>> {
        return repository.dailySaleTypeBackUp().asLiveData()
    }

    fun dailyNamesBackUp(): LiveData<List<String>> {
        return repository.dailyNamesBackUp().asLiveData()
    }

    fun dailyReceiptBackUp(): LiveData<List<String>> {
        return repository.dailyReceiptBackUp().asLiveData()
    }
}

class BackupViewModelFactory(private val repository: RoomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(BackupViewModel::class.java)) {
            return BackupViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}