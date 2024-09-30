package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.PaymentType

class PaymentTypesFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getPaymentTypes(): LiveData<List<PaymentType>> {
        return repository.getPaymentTypes().asLiveData()
    }
}

class PaymentTypesFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(PaymentTypesFragmentViewModel::class.java)) {
            return PaymentTypesFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}