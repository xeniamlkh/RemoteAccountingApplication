package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.Receipt
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ReceiptFragmentViewModel(private val repository: RoomRepository) : BaseViewModel(repository){

    fun createReceiptItem(
        dateCalc: Long,
        date: String,
        name: String,
        product: String,
        price: Double,
        number: Int
    ) {
        val receipt =
            Receipt(
                0,
                dateCalc,
                date,
                name,
                product,
                price,
                number
            )
        insertReceiptProduct(receipt)
    }

    fun createUpdateProductWithRemains(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifProductExist(product).firstOrNull()
            if (ifExist == true) {
                updateProductByProduct(product, price, remains, physical)
            } else {
                createProductNoteWithRemains(product, price, remains, physical)
            }
        }
    }

    private fun createProductNoteWithRemains(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        val note = Products(0, product, price, remains, physical)
        saveProductNote(note)
    }
}

class ReceiptFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ReceiptFragmentViewModel::class.java)) {
            return ReceiptFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}