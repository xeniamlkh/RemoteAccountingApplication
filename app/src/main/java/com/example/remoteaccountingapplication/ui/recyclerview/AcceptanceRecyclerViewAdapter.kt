package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.AcceptanceItemBinding
import com.example.remoteaccountingapplication.model.data.Acceptance

class AcceptanceRecyclerViewAdapter(
    private val acceptances: List<Acceptance>
) : RecyclerView.Adapter<AcceptanceRecyclerViewAdapter.AcceptanceViewHolder>() {

    inner class AcceptanceViewHolder(binding: AcceptanceItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptanceViewHolder {
        val binding = AcceptanceItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AcceptanceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return acceptances.size
    }

    override fun onBindViewHolder(holder: AcceptanceViewHolder, position: Int) {
        val item = acceptances[position]
        holder.title.text = item.product
        holder.price.text = item.price.toString()
        holder.remains.text = item.number.toString()
        holder.name.text = item.name
    }
}