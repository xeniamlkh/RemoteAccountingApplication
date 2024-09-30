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

class InsertFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getNameByLastId(): LiveData<String> {
        return repository.getNameByLastId().asLiveData()
    }

    fun getProducts(): LiveData<List<Products>> {
        return repository.getProducts().asLiveData()
    }

    fun getNames(): LiveData<List<Names>> {
        return repository.getNames().asLiveData()
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

    fun createSale(
        dateCalc: Long,
        date: String,
        product: String,
        price: Double,
        number: Int,
        paymentType: String,
        saleType: String,
        name: String,
        comment: String,
        total: Double
    ) {
        val sale =
            Sales(
                0,
                dateCalc,
                date,
                product,
                price,
                number,
                paymentType,
                saleType,
                name,
                comment,
                total
            )
        saveSale(sale)
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


    fun createProductNote(product: String, price: Double) {
        val note = Products(0, product, price, 0, false)
        saveProductNote(note)
    }

    fun updateProductItem(product: String, price: Double, remains: Int, physical: Boolean) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifProductExist(product).firstOrNull()
            if (ifExist == true) {
                if (physical) {
                    updateProductByProduct(product, price, remains, physical)
                }
            }
        }
    }

    private fun saveSale(sale: Sales) {
        viewModelScope.launch { repository.insertSale(sale) }
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

    private fun saveProductNote(note: Products) {
        viewModelScope.launch { repository.insertNote(note) }
    }

    private fun saveSaleType(type: SaleType) {
        viewModelScope.launch { repository.insertSaleType(type) }
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
}

class InsertFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(InsertFragmentViewModel::class.java)) {
            return InsertFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}