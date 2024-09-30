package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Names
import com.example.remoteaccountingapplication.data.room.PaymentType
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.SaleType
import kotlinx.coroutines.launch

class EditHandbookFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getProductById(itemId: Int): LiveData<Products> {
        return repository.getProductById(itemId).asLiveData()
    }

    fun getPaymentTypeById(itemId: Int): LiveData<PaymentType> {
        return repository.getPaymentTypeById(itemId).asLiveData()
    }

    fun getSaleTypeById(itemId: Int): LiveData<SaleType> {
        return repository.getSaleTypeById(itemId).asLiveData()
    }

    fun getNameById(itemId: Int): LiveData<Names> {
        return repository.getNameById(itemId).asLiveData()
    }

    fun updateProductById(
        itemId: Int,
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        viewModelScope.launch {
            repository.updateProductById(
                itemId,
                product,
                price,
                remains,
                physical
            )
        }
    }

    fun updatePaymentTypeById(itemId: Int, paymentType: String) {
        viewModelScope.launch {
            repository.updatePaymentTypeById(itemId, paymentType)
        }
    }

    fun updateSaleTypeById(itemId: Int, saleType: String) {
        viewModelScope.launch { repository.updateSaleTypeById(itemId, saleType) }
    }

    fun updateNameById(itemId: Int, name: String) {
        viewModelScope.launch { repository.updateNameById(itemId, name) }
    }

}

class EditHandbookFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EditHandbookFragmentViewModel::class.java)) {
            return EditHandbookFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}