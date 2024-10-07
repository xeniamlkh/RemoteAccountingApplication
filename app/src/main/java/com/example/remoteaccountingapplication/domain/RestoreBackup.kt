package com.example.remoteaccountingapplication.domain

import com.example.remoteaccountingapplication.data.room.Names
import com.example.remoteaccountingapplication.data.room.PaymentType
import com.example.remoteaccountingapplication.data.room.Products
import com.example.remoteaccountingapplication.data.room.Receipt
import com.example.remoteaccountingapplication.data.room.SaleType
import com.example.remoteaccountingapplication.data.room.Sales
import java.io.BufferedReader

class RestoreBackup {

    fun importSalesCsv(bufferedReader: BufferedReader): ArrayList<Sales> {

        lateinit var sale: Sales
        val salesList: ArrayList<Sales> = ArrayList()

        bufferedReader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 10) {
                val dateCalculation = tokens[0].trim().toLong()
                val date = tokens[1].trim()
                val product = tokens[2].trim()
                val price = tokens[3].trim().toDouble()
                val number = tokens[4].trim().toInt()
                val paymentType = tokens[5].trim()
                val saleType = tokens[6].trim()
                val name = tokens[7].trim()
                val comment = tokens[8].trim()
                val total = tokens[9].trim().toDouble()

                sale = Sales(
                    0,
                    dateCalculation,
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

                salesList.add(sale)

            }
        }

        return salesList
    }

    fun importProductsCsv(bufferedReader: BufferedReader): ArrayList<Products> {

        lateinit var productItem: Products
        val productsList: ArrayList<Products> = ArrayList()

        bufferedReader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 4) {
                val productName = tokens[0].trim()
                val price = tokens[1].trim().toDouble()
                val remains = tokens[2].trim().toInt()
                val physical = tokens[3].trim().toBoolean()

                productItem = Products(0, productName, price, remains, physical)

                productsList.add(productItem)
            }
        }

        return productsList
    }

    fun importPaymentTypesCsv(bufferedReader: BufferedReader): ArrayList<PaymentType> {

        lateinit var paymentType: PaymentType
        val paymentTypeList: ArrayList<PaymentType> = ArrayList()

        bufferedReader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 1) {
                val paymentTypeName = tokens[0].trim()
                paymentType = PaymentType(0, paymentTypeName)
                paymentTypeList.add(paymentType)
            }
        }

        return paymentTypeList
    }

    fun importSaleTypesCsv(bufferedReader: BufferedReader): ArrayList<SaleType> {

        lateinit var saleType: SaleType
        val saleTypeList: ArrayList<SaleType> = ArrayList()

        bufferedReader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 1) {
                val saleTypeName = tokens[0].trim()
                saleType = SaleType(0, saleTypeName)
                saleTypeList.add(saleType)
            }
        }

        return saleTypeList
    }

    fun importNamesCsv(bufferedReader: BufferedReader): ArrayList<Names> {

        lateinit var nameItem: Names
        val namesList: ArrayList<Names> = ArrayList()

        bufferedReader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 1) {
                val name = tokens[0].trim()
                nameItem = Names(0, name)
                namesList.add(nameItem)
            }
        }

        return namesList
    }

    fun importReceiptOfGoodsCsv(bufferedReader: BufferedReader): ArrayList<Receipt> {

        lateinit var receipt: Receipt
        val receiptList: ArrayList<Receipt> = ArrayList()

        bufferedReader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size == 6) {
                val dateCalculation = tokens[0].trim().toLong()
                val date = tokens[1].trim()
                val name = tokens[2].trim()
                val product = tokens[3].trim()
                val price = tokens[4].trim().toDouble()
                val number = tokens[5].trim().toInt()

                receipt = Receipt(0, dateCalculation, date, name, product, price, number)
                receiptList.add(receipt)

            }
        }

        return receiptList
    }
}