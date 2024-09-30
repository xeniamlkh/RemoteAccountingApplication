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
import kotlinx.coroutines.launch

class RestoreFragmentViewModel(private val repository: RoomRepository) : ViewModel() {

    fun getSalesRowsNumber(): LiveData<Int> {
        return repository.getSalesRowsNumber().asLiveData()
    }

    fun getProductsRowsNumber(): LiveData<Int> {
        return repository.getProductsRowsNumber().asLiveData()
    }

    fun getPaymentTypesRowsNumber(): LiveData<Int> {
        return repository.getPaymentTypesRowsNumber().asLiveData()
    }

    fun getSaleTypesRowsNumber(): LiveData<Int> {
        return repository.getSaleTypesRowsNumber().asLiveData()
    }

    fun getNamesRowsNumber(): LiveData<Int> {
        return repository.getNamesRowsNumber().asLiveData()
    }

    fun getReceiptRowsNumber(): LiveData<Int> {
        return repository.getReceiptRowsNumber().asLiveData()
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

    fun createPaymentTypeRecord(type: String) {
        val typeRecord = PaymentType(0, type)
        savePaymentType(typeRecord)
    }

    fun createSaleTypeRecord(type: String) {
        val typeRecord = SaleType(0, type)
        saveSaleType(typeRecord)
    }

    fun createProductNoteWithRemains(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        val note = Products(0, product, price, remains, physical)
        saveProductNote(note)
    }

    fun createNameRecord(name: String) {
        val nameRecord = Names(0, name)
        saveName(nameRecord)
    }

    fun createReceiptItem(
        dateCalc: Long,
        date: String,
        name: String,
        product: String,
        price: Double,
        number: Int
    ) {

        val receipt = Receipt(
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

    private fun saveSale(sale: Sales) {
        viewModelScope.launch { repository.insertSale(sale) }
    }

    private fun saveProductNote(note: Products) {
        viewModelScope.launch { repository.insertNote(note) }
    }

    private fun savePaymentType(type: PaymentType) {
        viewModelScope.launch { repository.insertPaymentType(type) }
    }

    private fun saveName(name: Names) {
        viewModelScope.launch { repository.insertName(name) }
    }

    private fun saveSaleType(type: SaleType) {
        viewModelScope.launch { repository.insertSaleType(type) }
    }

    private fun insertReceiptProduct(receiptProduct: Receipt) {
        viewModelScope.launch { repository.insertReceiptProduct(receiptProduct) }
    }
}

class RestoreFragmentViewModelFactory(private val repository: RoomRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(RestoreFragmentViewModel::class.java)) {
            return RestoreFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}