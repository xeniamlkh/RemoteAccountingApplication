package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.Names

class NamesRecyclerViewAdapter(
    private var namesList: List<Names>,
    private val menuClickListener: OnMenuClickListener
) :
    RecyclerView.Adapter<NamesRecyclerViewAdapter.NamesViewHolder>() {

    inner class NamesViewHolder(binding: HandbookItemBinding) :
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

    fun updateNamesList(newNamesList: List<Names>) {
        val diffCallback = AccountingDiffCallback(
            oldList = namesList,
            newList = newNamesList,
            areItemsTheSame = { old: Names, new: Names -> old.id == new.id },
            areContentsTheSame = { old: Names, new: Names -> old == new })
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        namesList = newNamesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NamesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return namesList.size
    }

    override fun onBindViewHolder(holder: NamesViewHolder, position: Int) {
        val nameItem = namesList[position]
        holder.title.text = nameItem.name
        holder.itemId = nameItem.id
    }
}