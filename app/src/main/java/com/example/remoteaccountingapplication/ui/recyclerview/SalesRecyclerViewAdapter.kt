package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.SaleItemBinding
import com.example.remoteaccountingapplication.data.room.Sales

class SalesRecyclerViewAdapter(
    private var salesList: List<Sales>,
    private val menuClickListener: OnMenuClickListener
) :
    RecyclerView.Adapter<SalesRecyclerViewAdapter.SalesViewHolder>() {

    inner class SalesViewHolder(binding: SaleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val product: TextView = binding.product
        val price: TextView = binding.price
        val number: TextView = binding.number
        val total: TextView = binding.total
        val paymentType: TextView = binding.paymentType
        val saleType: TextView = binding.saleType
        val name: TextView = binding.name
        val comment: TextView = binding.comment
        private val menuBtn: FrameLayout = binding.menuOptionsBtn
        var itemId: Int = -1

        init {
            menuBtn.setOnClickListener {
                menuClickListener.onMenuClick(it, itemId)
            }
        }
    }

    fun updateSalesList(newSalesList: List<Sales>) {
        val diffCallback = AccountingDiffCallback(
            oldList = salesList,
            newList = newSalesList,
            areItemsTheSame = { old: Sales, new: Sales -> old.id == new.id },
            areContentsTheSame = { old: Sales, new: Sales -> old == new })
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        salesList = newSalesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val binding = SaleItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SalesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return salesList.size
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val salesItem = salesList[position]

        holder.product.text = buildString {
            append("Товар: ")
            append(salesItem.product)
        }
        holder.price.text = buildString {
            append("Цена, руб.: ")
            append(salesItem.price.toString())
        }
        holder.number.text = buildString {
            append("Кол-во, шт.: ")
            append(salesItem.number.toString())
        }
        holder.total.text = buildString {
            append("Сумма, руб.: ")
            append(salesItem.total)
        }
        holder.paymentType.text = buildString {
            append("Тип оплаты: ")
            append(salesItem.paymentType)
        }
        holder.saleType.text = buildString {
            append("Тип продажи: ")
            append(salesItem.saleType)
        }
        holder.name.text = buildString {
            append("ФИО: ")
            append(salesItem.name)
        }
        holder.comment.text = buildString {
            append("Примечание: ")
            append(salesItem.comment)
        }
        holder.itemId = salesItem.id
    }
}