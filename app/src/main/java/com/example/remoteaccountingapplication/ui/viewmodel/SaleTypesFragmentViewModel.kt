package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.SaleType

class SaleTypesFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getSaleTypes(): LiveData<List<SaleType>> {
        return repository.getSaleTypes().asLiveData()
    }
}

class SaleTypesFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SaleTypesFragmentViewModel::class.java)) {
            return SaleTypesFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}