package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.PaymentType

class PaymentTypesRecyclerViewAdapter(
    private var paymentTypesList: List<PaymentType>,
    private val menuClickListener: OnMenuClickListener
) :
    RecyclerView.Adapter<PaymentTypesRecyclerViewAdapter.PaymentTypeViewHolder>() {

    inner class PaymentTypeViewHolder(binding: HandbookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title: TextView = binding.title
        private val menuBtn: FrameLayout = binding.menuOptionsBtn
        var itemId: Int = -1

        init {
            menuBtn.setOnClickListener {
                menuClickListener.onMenuClick(it, itemId)
            }
        }

    }

    fun updatePaymentTypesList(newPaymentTypesList: List<PaymentType>) {
        val diffCallback = AccountingDiffCallback(
            oldList = paymentTypesList,
            newList = newPaymentTypesList,
            areItemsTheSame = { old: PaymentType, new: PaymentType -> old.id == new.id },
            areContentsTheSame = { old: PaymentType, new: PaymentType -> old == new })
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        paymentTypesList = newPaymentTypesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentTypeViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paymentTypesList.size
    }

    override fun onBindViewHolder(holder: PaymentTypeViewHolder, position: Int) {
        val paymentTypeItem = paymentTypesList[position]
        holder.title.text = paymentTypeItem.paymentType
        holder.itemId = paymentTypeItem.id
    }
}