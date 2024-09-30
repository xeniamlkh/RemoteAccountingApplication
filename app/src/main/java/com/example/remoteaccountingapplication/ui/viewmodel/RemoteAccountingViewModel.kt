package com.example.remoteaccountingapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.room.Receipt
import com.example.remoteaccountingapplication.data.room.Names
import com.example.remoteaccountingapplication.data.room.PaymentType
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.SaleType
import com.example.remoteaccountingapplication.data.room.Sales
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RemoteAccountingViewModel(private val repository: RoomRepository, application: Application) :
    ViewModel() {

    private val _startDateLivaData = MutableLiveData<String>()
    val startDateLiveData: LiveData<String> get() = _startDateLivaData

    private val _endDateLivaData = MutableLiveData<String>()
    val endDateLiveData: LiveData<String> get() = _endDateLivaData

    private var startDateVM: Long? = null
    private var endDateVM: Long? = null


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

    private fun saveSale(sale: Sales) {
        viewModelScope.launch { repository.insertSale(sale) }
    }

    fun getListOfSalesByDate(startOfDay: Long, endOfDay: Long): LiveData<List<Sales>> {
        return repository.getListOfSalesByDate(startOfDay, endOfDay).asLiveData()
    }

    fun exportRangeSalesCsv(): LiveData<List<String>> {
        return repository.exportRangeSalesCsv(startDateVM!!, endDateVM!!).asLiveData() // null
    }

    fun deleteSaleItemById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteSaleItemById(itemId)
        }
    }

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

    fun getSalesRowsNumber(): LiveData<Int> {
        return repository.getSalesRowsNumber().asLiveData()
    }

    fun getTotalSum(startOfDay: Long, endOfDay: Long): LiveData<Double> {
        return repository.getTotalSum(startOfDay, endOfDay).asLiveData()
    }

    fun getNameByLastId(): LiveData<String> {
        return repository.getNameByLastId().asLiveData()
    }


    fun createProductNote(product: String, price: Double) {
        val note = Products(0, product, price, 0, false)
        saveProductNote(note)
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

    private fun saveProductNote(note: Products) {
        viewModelScope.launch { repository.insertNote(note) }
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

    fun updateProductRemains(product: String, remains: Int) {
        if (remains >= 0) {
            updateProductNumberByProduct(product, remains)
        } else {
            updateProductNumberByProduct(product, 0)
        }
    }

    private fun updateProductNumberByProduct(product: String, remains: Int) {
        viewModelScope.launch {
            repository.updateProductNumberByProduct(product, remains)
        }
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

    fun getProducts(): LiveData<List<Products>> {
        return repository.getProducts().asLiveData()
    }

    fun ifProductExist(product: String): LiveData<Boolean> {
        return repository.ifProductExist(product).asLiveData()
    }

    fun getProductByProductName(product: String): LiveData<Products> {
        return repository.getProductByProductName(product).asLiveData()
    }

    fun deleteProductById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteProductById(itemId)
        }
    }

    fun getProductById(itemId: Int): LiveData<Products> {
        return repository.getProductById(itemId).asLiveData()
    }

    fun getProductsRowsNumber(): LiveData<Int> {
        return repository.getProductsRowsNumber().asLiveData()
    }


    private fun savePaymentType(type: PaymentType) {
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

    fun createPaymentTypeRecord(type: String) {
        val typeRecord = PaymentType(0, type)
        savePaymentType(typeRecord)
    }

    fun deletePaymentTypeById(itemId: Int) {
        viewModelScope.launch {
            repository.deletePaymentTypeById(itemId)
        }
    }

    fun getPaymentTypeById(itemId: Int): LiveData<PaymentType> {
        return repository.getPaymentTypeById(itemId).asLiveData()
    }

    fun updatePaymentTypeById(itemId: Int, paymentType: String) {
        viewModelScope.launch {
            repository.updatePaymentTypeById(itemId, paymentType)
        }
    }

    fun getPaymentTypesRowsNumber(): LiveData<Int> {
        return repository.getPaymentTypesRowsNumber().asLiveData()
    }


    private fun saveSaleType(type: SaleType) {
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

    fun createSaleTypeRecord(type: String) {
        val typeRecord = SaleType(0, type)
        saveSaleType(typeRecord)
    }

    fun deleteSaleTypeById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteSaleTypeById(itemId)
        }
    }

    fun getSaleTypeById(itemId: Int): LiveData<SaleType> {
        return repository.getSaleTypeById(itemId).asLiveData()
    }

    fun updateSaleTypeById(itemId: Int, saleType: String) {
        viewModelScope.launch { repository.updateSaleTypeById(itemId, saleType) }
    }

    fun getSaleTypesRowsNumber(): LiveData<Int> {
        return repository.getSaleTypesRowsNumber().asLiveData()
    }


    private fun saveName(name: Names) {
        viewModelScope.launch { repository.insertName(name) }
    }

    fun getNames(): LiveData<List<Names>> {
        return repository.getNames().asLiveData()
    }

    fun createNameRecord(name: String) {
        val nameRecord = Names(0, name)
        saveName(nameRecord)
    }

    fun createNameRecordIfNotExist(name: String) {
        viewModelScope.launch {
            val ifExist: Boolean? = repository.ifNameExist(name).firstOrNull()
            if (ifExist != true) {
                createNameRecord(name)
            }
        }
    }

    fun deleteNameById(itemId: Int) {
        viewModelScope.launch {
            repository.deleteNameById(itemId)
        }
    }

    fun getNameById(itemId: Int): LiveData<Names> {
        return repository.getNameById(itemId).asLiveData()
    }

    fun updateNameById(itemId: Int, name: String) {
        viewModelScope.launch { repository.updateNameById(itemId, name) }
    }

    fun getNamesRowsNumber(): LiveData<Int> {
        return repository.getNamesRowsNumber().asLiveData()
    }

    fun clearRange() {
        startDateVM = null
        endDateVM = null

        _startDateLivaData.value = ""
        _endDateLivaData.value = ""
    }

    fun setStartDate(startDate: Long?) {
        if (startDate != null) {
            startDateVM = startDate

            val simpleCalendarDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendarDate: String = simpleCalendarDate.format(startDate)

            _startDateLivaData.value = calendarDate
        }
    }

    fun setEndDate(endDate: Long?) {
        if (endDate != null) {
            endDateVM = endDate

            val simpleCalendarDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendarDate: String = simpleCalendarDate.format(endDate)

            _endDateLivaData.value = calendarDate
        }
    }


    fun createAcceptanceItem(
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
        insertAcceptProduct(receipt)
    }

    private fun insertAcceptProduct(acceptProduct: Receipt) {
        viewModelScope.launch { repository.insertAcceptProduct(acceptProduct) }
    }

    fun getListOfAcceptancesByDate(
        startOfDay: Long,
        endOfDay: Long
    ): LiveData<List<Receipt>> {
        return repository.getListOfAcceptancesByDate(startOfDay, endOfDay).asLiveData()
    }

    fun getAcceptanceRowsNumber(): LiveData<Int> {
        return repository.getAcceptanceRowsNumber().asLiveData()
    }
}

class RemoteAccountingViewModelFactory(
    private val repository: RoomRepository,
    private val application: Application
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(RemoteAccountingViewModel::class.java)) {
            return RemoteAccountingViewModel(repository, application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}