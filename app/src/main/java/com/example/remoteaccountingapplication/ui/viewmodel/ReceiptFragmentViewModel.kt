package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Names
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.Receipt
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ReceiptFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getProducts(): LiveData<List<Products>> {
        return repository.getProducts().asLiveData()
    }

    fun getProductByProductName(product: String): LiveData<Products> {
        return repository.getProductByProductName(product).asLiveData()
    }

    fun getListOfReceiptByDate(startOfDay: Long, endOfDay: Long): LiveData<List<Receipt>> {
        return repository.getListOfReceiptByDate(startOfDay, endOfDay).asLiveData()
    }

    fun createNameRecordIfNotExist(name: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifNameExist(name).firstOrNull()
            if (ifExist != true) {
                createNameRecord(name)
            }
        }
    }

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

    private fun createNameRecord(name: String) {
        val nameRecord = Names(0, name)
        saveName(nameRecord)
    }

    private fun saveName(name: Names) {
        viewModelScope.launch { repository.insertName(name) }
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

    private fun saveProductNote(note: Products) {
        viewModelScope.launch { repository.insertNote(note) }
    }

    private fun updateProductByProduct(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        viewModelScope.launch {
            repository.updateProductByProduct(product, price, remains, physical)
        }
    }

    private fun insertReceiptProduct(receiptProduct: Receipt) {
        viewModelScope.launch { repository.insertReceiptProduct(receiptProduct) }
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