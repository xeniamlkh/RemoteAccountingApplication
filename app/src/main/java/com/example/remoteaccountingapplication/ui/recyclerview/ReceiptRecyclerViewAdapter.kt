package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.data.room.Receipt
import com.example.remoteaccountingapplication.databinding.ReceiptItemBinding

class ReceiptRecyclerViewAdapter(
    private var receiptList: List<Receipt>
) : RecyclerView.Adapter<ReceiptRecyclerViewAdapter.ReceiptViewHolder>() {

    inner class ReceiptViewHolder(binding: ReceiptItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title: TextView = binding.title
        val price: TextView = binding.price
        val remains: TextView = binding.remains
        val name: TextView = binding.name

    }

    fun updateReceiptList(newReceiptList: List<Receipt>) {
        val diffCallback = AccountingDiffCallback(
            oldList = receiptList,
            newList = newReceiptList,
            areItemsTheSame = { old: Receipt, new: Receipt -> old.id == new.id },
            areContentsTheSame = { old: Receipt, new: Receipt -> old == new })
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        receiptList = newReceiptList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val binding = ReceiptItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiptViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return receiptList.size
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = receiptList[position]
        holder.title.text = item.product
        holder.price.text = item.price.toString()
        holder.remains.text = item.number.toString()
        holder.name.text = item.name
    }
}