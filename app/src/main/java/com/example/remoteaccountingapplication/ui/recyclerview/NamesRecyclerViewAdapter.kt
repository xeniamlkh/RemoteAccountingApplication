package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.Names

class NamesRecyclerViewAdapter(
    private val names: List<Names>,
    private val menuClickListener: OnMenuClickListener
) :
    RecyclerView.Adapter<NamesRecyclerViewAdapter.NamesViewHolder>() {

    inner class NamesViewHolder(binding: HandbookItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NamesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: NamesViewHolder, position: Int) {
        val nameItem = names[position]
        holder.title.text = nameItem.name
        holder.itemId = nameItem.id
    }
}