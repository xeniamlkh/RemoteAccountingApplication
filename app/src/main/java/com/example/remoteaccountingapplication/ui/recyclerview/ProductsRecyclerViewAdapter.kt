package com.example.remoteaccountingapplication.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteaccountingapplication.databinding.HandbookItemBinding
import com.example.remoteaccountingapplication.data.room.Products

class ProductsRecyclerViewAdapter(
    private var productsList: List<Products>,
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

    fun updateProductsList(newProductsList: List<Products>) {
        val diffCallback = AccountingDiffCallback(
            oldList = productsList,
            newList = newProductsList,
            areItemsTheSame = { old: Products, new: Products -> old.id == new.id },
            areContentsTheSame = { old: Products, new: Products -> old == new })
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        productsList = newProductsList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = HandbookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val noteItem = productsList[position]
        holder.title.text = noteItem.product
        holder.price.text = noteItem.price.toString()
        holder.remains.text = noteItem.remains.toString()
        holder.itemId = noteItem.id
    }
}