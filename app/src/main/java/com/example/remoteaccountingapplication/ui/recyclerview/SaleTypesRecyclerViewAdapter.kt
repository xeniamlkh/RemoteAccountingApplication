package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.SaleType

class SaleTypesRecyclerViewAdapter(
    private var saleTypesList: List<SaleType>,
    private val menuClickListener: OnMenuClickListener
) : RecyclerView.Adapter<SaleTypesRecyclerViewAdapter.SaleTypeViewHolder>() {

    inner class SaleTypeViewHolder(binding: HandbookItemBinding) :
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

    fun updateSaleTypesList(newSaleTypesList: List<SaleType>) {
        val diffCallback = AccountingDiffCallback(
            oldList = saleTypesList,
            newList = newSaleTypesList,
            areItemsTheSame = { old: SaleType, new: SaleType -> old.id == new.id },
            areContentsTheSame = { old: SaleType, new: SaleType -> old == new })
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        saleTypesList = newSaleTypesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleTypeViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SaleTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return saleTypesList.size
    }

    override fun onBindViewHolder(holder: SaleTypeViewHolder, position: Int) {
        val saleTypeItem = saleTypesList[position]
        holder.title.text = saleTypeItem.saleType
        holder.itemId = saleTypeItem.id
    }
}