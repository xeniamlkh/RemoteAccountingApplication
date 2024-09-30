package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.data.room.Receipt
import com.example.remoteaccountingapplication.databinding.ReceiptItemBinding

class ReceiptRecyclerViewAdapter(
    private val receipts: List<Receipt>
) : RecyclerView.Adapter<ReceiptRecyclerViewAdapter.ReceiptViewHolder>() {

    inner class ReceiptViewHolder(binding: ReceiptItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title: TextView
        val price: TextView
        val remains: TextView
        val name: TextView

        init {
            title = binding.title
            price = binding.price
            remains = binding.remains
            name = binding.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val binding = ReceiptItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiptViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return receipts.size
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = receipts[position]
        holder.title.text = item.product
        holder.price.text = item.price.toString()
        holder.remains.text = item.number.toString()
        holder.name.text = item.name
    }
}