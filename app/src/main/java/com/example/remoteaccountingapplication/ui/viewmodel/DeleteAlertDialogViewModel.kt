package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DeleteAlertDialogViewModel(private val repository: RoomRepository) : ViewModel() {

    fun deleteSaleItemById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteSaleItemById(itemId)
        }
    }

    fun deleteProductById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteProductById(itemId)
        }
    }

    fun deletePaymentTypeById(itemId: Int) {
        viewModelScope.launch {
            repository.deletePaymentTypeById(itemId)
        }
    }

    fun deleteSaleTypeById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteSaleTypeById(itemId)
        }
    }

    fun deleteNameById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteNameById(itemId)
        }
    }

    fun restoreProductRemains(itemId: Int) {
        viewModelScope.launch {
            val saleItem = repository.getSaleItemById(itemId).firstOrNull()
            val salesItemProduct: String? = saleItem?.product
            val salesItemNumber: Int? = saleItem?.number

            if (salesItemProduct != null) {
                val productItem = repository.getProductByProductName(salesItemProduct).firstOrNull()
                val productRemains: Int? = productItem?.remains
                val productPhysical: Boolean? = productItem?.physical

                if (productPhysical == true) {
                    val restoredNumber = productRemains?.let { salesItemNumber?.plus(it) }
                    if (restoredNumber != null) {
                        updateProductNumberByProduct(salesItemProduct, restoredNumber)
                    }
                }
            }
        }
    }

    private fun updateProductNumberByProduct(product: String, remains: Int) {
        viewModelScope.launch {
            repository.updateProductNumberByProduct(product, remains)
        }
    }
}

class DeleteAlertDialogViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DeleteAlertDialogViewModel::class.java)) {
            return DeleteAlertDialogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}