package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Names

class NamesFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getNames(): LiveData<List<Names>> {
        return repository.getNames().asLiveData()
    }
}

class NamesFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(NamesFragmentViewModel::class.java)) {
            return NamesFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}