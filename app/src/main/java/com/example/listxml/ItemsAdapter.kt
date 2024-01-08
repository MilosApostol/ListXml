package com.example.listxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.databinding.ItemRvBinding

class ItemsAdapter(
    val items: List<ItemsEntity>,
    val itemsId: List<String>,
    private var itemClickListener: ItemsAdapter.ItemClickListener,

    ) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsEntity) {
            binding.textName.text = item.name

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(item, itemsId[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRvBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    interface ItemClickListener {
        fun onItemClick(itemName: ItemsEntity, itemId: String)
    }
}