package com.example.remoteaccountingapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.remoteaccountingapplication.data.repository.RoomRepository
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.Sales
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class InsertFragmentViewModel(private val repository: RoomRepository) : BaseViewModel(repository) {

    fun getNameByLastId(): LiveData<String> {
        return repository.getNameByLastId().asLiveData()
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