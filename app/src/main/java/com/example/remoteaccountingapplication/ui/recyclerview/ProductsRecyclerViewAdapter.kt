package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.Products

class ProductsRecyclerViewAdapter(
    private val products: List<Products>,
    private val menuClickListener: OnMenuClickListener
) :
    RecyclerView.Adapter<ProductsRecyclerViewAdapter.ProductsViewHolder>() {

    inner class ProductsViewHolder(binding: HandbookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title: TextView
        val price: TextView
        val remains: TextView
        val menuBtn: FrameLayout
        var itemId: Int = -1

        init {
            title = binding.title
            price = binding.price
            remains = binding.remains
            menuBtn = binding.menuOptionsBtn

            menuBtn.setOnClickListener {
                if (adapterPosition >= 0) {
                    menuClickListener.onMenuClick(it, itemId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val noteItem = products[position]
        holder.title.text = noteItem.product
        holder.price.text = noteItem.price.toString()
        holder.remains.text = noteItem.remains.toString()
        holder.itemId = noteItem.id
    }
}