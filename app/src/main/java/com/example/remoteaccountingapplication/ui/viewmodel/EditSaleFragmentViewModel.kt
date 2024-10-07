package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Sales
import kotlinx.coroutines.launch

class EditSaleFragmentViewModel(private val repository: RoomRepository) : BaseViewModel(repository) {

    fun getSaleItemById(itemId: Int): LiveData<Sales> {
        return repository.getSaleItemById(itemId).asLiveData() // map + model
    }

    fun updateSaleItemById(
        itemId: Int,
        product: String,
        price: Double,
        number: Int,
        paymentType: String,
        saleType: String,
        name: String,
        comment: String,
        total: Double
    ) {
        viewModelScope.launch {
            repository.updateSaleItemById(
                itemId,
                product,
                price,
                number,
                paymentType,
                saleType,
                name,
                comment,
                total
            )
        }
    }

    fun updateProductRemains(product: String, remains: Int) {
        if (remains >= 0) {
            updateProductNumberByProduct(product, remains)
        } else {
            updateProductNumberByProduct(product, 0)
        }
    }
}

class EditSaleFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EditSaleFragmentViewModel::class.java)) {
            return EditSaleFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}