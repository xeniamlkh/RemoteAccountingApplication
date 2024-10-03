package com.example.remoteaccountingapplication.data.repository

import com.example.remoteaccountingapplication.data.room.Receipt
import com.example.remoteaccountingapplication.data.room.ReceiptDao
import com.example.remoteaccountingapplication.data.room.Names
import com.example.remoteaccountingapplication.data.room.NamesDao
import com.example.remoteaccountingapplication.data.room.PaymentType
import com.example.remoteaccountingapplication.data.room.PaymentTypeDao
import com.example.remoteaccountingapplication.data.room.ProductsDao
import com.example.remoteaccountingapplication.data.room.SalesDao
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.SaleType
import com.example.remoteaccountingapplication.data.room.SaleTypeDao
import com.example.remoteaccountingapplication.data.room.Sales
import kotlinx.coroutines.flow.Flow

class RoomRepository(
    private val salesDao: SalesDao,
    private val productsDao: ProductsDao,
    private val namesDao: NamesDao,
    private val paymentTypeDao: PaymentTypeDao,
    private val saleTypeDao: SaleTypeDao,
    private val receiptDao: ReceiptDao
) {

    suspend fun insertSale(sale: Sales) {
        salesDao.insertSale(sale)
    }

    fun getListOfSalesByDate(startOfDay: Long, endOfDay: Long): Flow<List<Sales>> {
        return salesDao.getListOfSalesByDate(startOfDay, endOfDay)
    }

    fun exportMonthSalesCsv(startDate: Long): Flow<List<String>> {
        return salesDao.exportMonthSalesCsv(startDate)
    }

    fun exportRangeSalesCsv(startDate: Long, endDate: Long): Flow<List<String>> {
        return salesDao.exportRangeSalesCsv(startDate, endDate)
    }

    fun getTotalSum(startOfDay: Long, endOfDay: Long): Flow<Double> {
        return salesDao.getTotalSum(startOfDay, endOfDay)
    }

    suspend fun deleteSaleItemById(itemId: Int) {
        salesDao.deleteSaleItemById(itemId)
    }

    fun getSaleItemById(itemId: Int): Flow<Sales> {
        return salesDao.getSaleItemById(itemId)
    }

    suspend fun updateSaleItemById(
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
        salesDao.updateSaleItemById(
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

    fun getSalesRowsNumber(): Flow<Int> {
        return salesDao.getSalesRowsNumber()
    }

    fun getNameByLastId(): Flow<String> {
        return salesDao.getNameByLastId()
    }

    suspend fun insertNote(note: Products) {
        productsDao.insertNote(note)
    }

    fun getProducts(): Flow<List<Products>> {
        return productsDao.getProducts()
    }

    fun ifProductExist(product: String): Flow<Boolean> {
        return productsDao.ifProductExist(product)
    }

    suspend fun deleteProductById(itemId: Int) {
        productsDao.deleteProductById(itemId)
    }

    suspend fun updateProductById(
        itemId: Int,
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        productsDao.updateProductById(itemId, product, price, remains, physical)
    }

    fun getProductById(itemId: Int): Flow<Products> {
        return productsDao.getProductById(itemId)
    }

    fun getProductsRowsNumber(): Flow<Int> {
        return productsDao.getProductsRowsNumber()
    }

    fun getProductByProductName(product: String): Flow<Products> {
        return productsDao.getProductByProductName(product)
    }

    suspend fun updateProductByProduct(
        product: String,
        price: Double,
        remains: Int,
        physical: Boolean
    ) {
        productsDao.updateProductByProduct(product, price, remains, physical)
    }

    suspend fun updateProductNumberByProduct(product: String, remains: Int) {
        productsDao.updateProductNumberByProduct(product, remains)
    }

    suspend fun insertName(name: Names) {
        namesDao.insertName(name)
    }

    fun getNames(): Flow<List<Names>> {
        return namesDao.getNames()
    }

    fun ifNameExist(name: String): Flow<Boolean> {
        return namesDao.ifNameExist(name)
    }

    suspend fun deleteNameById(itemId: Int) {
        namesDao.deleteNameById(itemId)
    }

    fun getNameById(itemId: Int): Flow<Names> {
        return namesDao.getNameById(itemId)
    }

    suspend fun updateNameById(itemId: Int, name: String) {
        namesDao.updateNameById(itemId, name)
    }

    fun getNamesRowsNumber(): Flow<Int> {
        return namesDao.getNamesRowsNumber()
    }

    suspend fun insertPaymentType(type: PaymentType) {
        paymentTypeDao.insertPaymentType(type)
    }

    fun getPaymentTypes(): Flow<List<PaymentType>> {
        return paymentTypeDao.getPaymentTypes()
    }

    fun ifPaymentTypeExist(type: String): Flow<Boolean> {
        return paymentTypeDao.ifPaymentTypeExist(type)
    }

    suspend fun deletePaymentTypeById(itemId: Int) {
        paymentTypeDao.deletePaymentTypeById(itemId)
    }

    fun getPaymentTypeById(itemId: Int): Flow<PaymentType> {
        return paymentTypeDao.getPaymentTypeById(itemId)
    }

    suspend fun updatePaymentTypeById(itemId: Int, paymentType: String) {
        paymentTypeDao.updatePaymentTypeById(itemId, paymentType)
    }

    fun getPaymentTypesRowsNumber(): Flow<Int> {
        return paymentTypeDao.getPaymentTypesRowsNumber()
    }


    suspend fun insertSaleType(type: SaleType) {
        saleTypeDao.insertSaleType(type)
    }

    fun getSaleTypes(): Flow<List<SaleType>> {
        return saleTypeDao.getSaleTypes()
    }

    fun ifSaleTypeExist(type: String): Flow<Boolean> {
        return saleTypeDao.ifSaleTypeExist(type)
    }

    suspend fun deleteSaleTypeById(itemId: Int) {
        saleTypeDao.deleteSaleTypeById(itemId)
    }

    fun getSaleTypeById(itemId: Int): Flow<SaleType> {
        return saleTypeDao.getSaleTypeById(itemId)
    }

    suspend fun updateSaleTypeById(itemId: Int, saleType: String) {
        saleTypeDao.updateSaleTypeById(itemId, saleType)
    }

    fun getSaleTypesRowsNumber(): Flow<Int> {
        return saleTypeDao.getSaleTypesRowsNumber()
    }


    suspend fun insertReceiptProduct(receiptProduct: Receipt) {
        receiptDao.insertReceiptProduct(receiptProduct)
    }

    fun getListOfReceiptByDate(
        startOfDay: Long,
        endOfDay: Long
    ): Flow<List<Receipt>> {
        return receiptDao.getListOfReceiptByDate(startOfDay, endOfDay)
    }

    fun getReceiptRowsNumber(): Flow<Int> {
        return receiptDao.getReceiptRowsNumber()
    }

    fun dailyAllSalesBackUp(): Flow<List<String>> {
        return salesDao.dailyAllSalesBackUp()
    }

    fun dailyProductsBackUp(): Flow<List<String>> {
        return productsDao.dailyProductsBackUp()
    }

    fun dailyPaymentTypeBackUp(): Flow<List<String>> {
        return paymentTypeDao.dailyPaymentTypeBackUp()
    }

    fun dailySaleTypeBackUp(): Flow<List<String>> {
        return saleTypeDao.dailySaleTypeBackUp()
    }

    fun dailyNamesBackUp(): Flow<List<String>> {
        return namesDao.dailyNamesBackUp()
    }

    fun dailyReceiptBackUp(): Flow<List<String>> {
        return receiptDao.dailyReceiptBackUpFlow()
    }
}