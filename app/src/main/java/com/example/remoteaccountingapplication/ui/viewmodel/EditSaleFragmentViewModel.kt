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
import com.example.remoteaccountingapplication.data.room.Sales
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EditSaleFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getSaleItemById(itemId: Int): LiveData<Sales> {
        return repository.getSaleItemById(itemId).asLiveData() // map + model
    }

    fun getProductByProductName(product: String): LiveData<Products> {
        return repository.getProductByProductName(product).asLiveData()
    }

    fun getPaymentTypes(): LiveData<List<PaymentType>> {
        return repository.getPaymentTypes().asLiveData()
    }

    fun getSaleTypes(): LiveData<List<SaleType>> {
        return repository.getSaleTypes().asLiveData()
    }

    fun getNames(): LiveData<List<Names>> {
        return repository.getNames().asLiveData()
    }

    fun ifProductExist(product: String): LiveData<Boolean> {
        return repository.ifProductExist(product).asLiveData()
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

    fun createNameRecordIfNotExist(name: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifNameExist(name).firstOrNull()
            if (ifExist != true) {
                createNameRecord(name)
            }
        }
    }

    fun createPaymentTypeIfNotExist(type: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifPaymentTypeExist(type).firstOrNull()
            if (ifExist != true) {
                createPaymentTypeRecord(type)
            }
        }
    }

    fun createSaleTypeIfNotExist(type: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifSaleTypeExist(type).firstOrNull()
            if (ifExist != true) {
                createSaleTypeRecord(type)
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

    private fun createPaymentTypeRecord(type: String) {
        val typeRecord = PaymentType(0, type)
        savePaymentType(typeRecord)
    }

    private fun savePaymentType(type: PaymentType) {
        viewModelScope.launch { repository.insertPaymentType(type) }
    }

    private fun createSaleTypeRecord(type: String) {
        val typeRecord = SaleType(0, type)
        saveSaleType(typeRecord)
    }

    private fun saveSaleType(type: SaleType) {
        viewModelScope.launch { repository.insertSaleType(type) }
    }

    private fun updateProductNumberByProduct(product: String, remains: Int) {
        viewModelScope.launch {
            repository.updateProductNumberByProduct(product, remains)
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