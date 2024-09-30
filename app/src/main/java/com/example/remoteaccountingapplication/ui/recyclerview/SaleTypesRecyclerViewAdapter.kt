package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.SaleType

class SaleTypesRecyclerViewAdapter(
    private val saleTypes: List<SaleType>,
    private val menuClickListener: OnMenuClickListener
) : RecyclerView.Adapter<SaleTypesRecyclerViewAdapter.SaleTypeViewHolder>() {

    inner class SaleTypeViewHolder(binding: HandbookItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleTypeViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SaleTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return saleTypes.size
    }

    override fun onBindViewHolder(holder: SaleTypeViewHolder, position: Int) {
        val saleTypeItem = saleTypes[position]
        holder.title.text = saleTypeItem.saleType
        holder.itemId = saleTypeItem.id
    }
}