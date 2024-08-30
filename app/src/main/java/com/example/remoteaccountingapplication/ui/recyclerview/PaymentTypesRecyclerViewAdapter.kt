package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.model.data.PaymentType

class PaymentTypesRecyclerViewAdapter(
    private val paymentTypes: List<PaymentType>,
    private val menuClickListener: OnMenuClickListener
) :
    RecyclerView.Adapter<PaymentTypesRecyclerViewAdapter.PaymentTypeViewHolder>() {

    inner class PaymentTypeViewHolder(binding: HandbookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title: TextView
        val menuBtn: FrameLayout
        var itemId: Int = -1

        init {
            title = binding.title
            menuBtn = binding.menuOptionsBtn

            menuBtn.setOnClickListener {
                if (adapterPosition >= 0) {
                    menuClickListener.onMenuClick(it, itemId)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentTypeViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paymentTypes.size
    }

    override fun onBindViewHolder(holder: PaymentTypeViewHolder, position: Int) {
        val paymentTypeItem = paymentTypes[position]
        holder.title.text = paymentTypeItem.paymentType
        holder.itemId = paymentTypeItem.id
    }
}