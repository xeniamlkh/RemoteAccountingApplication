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
import com.example.remoteaccountingapplication.data.room.Receipt
import com.example.remoteaccountingapplication.data.room.SaleType
import com.example.remoteaccountingapplication.data.room.Sales
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

open class BaseViewModel(private val repository: RoomRepository): ViewModel()  {

    fun saveSale(sale: Sales) {
        viewModelScope.launch { repository.insertSale(sale) }
    }

    fun saveProductNote(note: Products) {
        viewModelScope.launch { repository.insertNote(note) }
    }

    fun getProducts(): LiveData<List<Products>> {
        return repository.getProducts().asLiveData()
    }

    fun ifProductExist(product: String): LiveData<Boolean> {
        return repository.ifProductExist(product).asLiveData()
    }

    fun getProductByProductName(product: String): LiveData<Products> {
        return repository.getProductByProductName(product).asLiveData()
    }

    fun updateProductNumberByProduct(product: String, remains: Int) {
        viewModelScope.launch {
            repository.updateProductNumberByProduct(product, remains)
        }
    }

    fun updateProductByProduct(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        viewModelScope.launch {
            repository.updateProductByProduct(product, price, remains, physical)
        }
    }

    fun saveName(name: Names) {
        viewModelScope.launch { repository.insertName(name) }
    }

    fun getNames(): LiveData<List<Names>> {
        return repository.getNames().asLiveData()
    }

    fun createNameRecordIfNotExist(name: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifNameExist(name).firstOrNull()
            if (ifExist != true) {
                createNameRecord(name)
            }
        }
    }

    private fun createNameRecord(name: String) {
        val nameRecord = Names(0, name)
        saveName(nameRecord)
    }

    fun savePaymentType(type: PaymentType) {
        viewModelScope.launch { repository.insertPaymentType(type) }
    }

    fun getPaymentTypes(): LiveData<List<PaymentType>> {
        return repository.getPaymentTypes().asLiveData()
    }

    fun createPaymentTypeIfNotExist(type: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifPaymentTypeExist(type).firstOrNull()
            if (ifExist != true) {
                createPaymentTypeRecord(type)
            }
        }
    }

    private fun createPaymentTypeRecord(type: String) {
        val typeRecord = PaymentType(0, type)
        savePaymentType(typeRecord)
    }

    fun saveSaleType(type: SaleType) {
        viewModelScope.launch { repository.insertSaleType(type) }
    }

    fun getSaleTypes(): LiveData<List<SaleType>> {
        return repository.getSaleTypes().asLiveData()
    }

    fun createSaleTypeIfNotExist(type: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifSaleTypeExist(type).firstOrNull()
            if (ifExist != true) {
                createSaleTypeRecord(type)
            }
        }
    }

    private fun createSaleTypeRecord(type: String) {
        val typeRecord = SaleType(0, type)
        saveSaleType(typeRecord)
    }

    fun insertReceiptProduct(receiptProduct: Receipt) {
        viewModelScope.launch { repository.insertReceiptProduct(receiptProduct) }
    }

    fun getListOfReceiptByDate(startOfDay: Long, endOfDay: Long): LiveData<List<Receipt>> {
        return repository.getListOfReceiptByDate(startOfDay, endOfDay).asLiveData()
    }
}

class BaseViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
            return BaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}