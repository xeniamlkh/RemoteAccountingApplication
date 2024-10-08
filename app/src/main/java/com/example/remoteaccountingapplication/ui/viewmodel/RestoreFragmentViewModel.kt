package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository

class RestoreFragmentViewModel(private val repository: RoomRepository) : BaseViewModel(repository) {

    fun getSalesRowsNumber(): LiveData<Int> {
        return repository.getSalesRowsNumber().asLiveData()
    }

    fun getProductsRowsNumber(): LiveData<Int> {
        return repository.getProductsRowsNumber().asLiveData()
    }

    fun getPaymentTypesRowsNumber(): LiveData<Int> {
        return repository.getPaymentTypesRowsNumber().asLiveData()
    }

    fun getSaleTypesRowsNumber(): LiveData<Int> {
        return repository.getSaleTypesRowsNumber().asLiveData()
    }

    fun getNamesRowsNumber(): LiveData<Int> {
        return repository.getNamesRowsNumber().asLiveData()
    }

    fun getReceiptRowsNumber(): LiveData<Int> {
        return repository.getReceiptRowsNumber().asLiveData()
    }
}

class RestoreFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(RestoreFragmentViewModel::class.java)) {
            return RestoreFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}