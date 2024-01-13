package com.example.listxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.databinding.ItemRvBinding
import java.util.Collections

class ItemsAdapter(
    var items: List<ItemsEntity>,
    val itemsId: List<String>,
    private var itemClickListener: ItemsAdapter.ItemClickListener,

    ) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    var onDelete: (ItemsEntity) -> Unit = {}



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
    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getItemsId(position: Int): ItemsEntity{
        return items[position]
    }
    fun submitList(newList: List<ItemsEntity>) {
        items = newList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        val item = items[position]
        onDelete(item)

    }

    interface ItemClickListener {
        fun onItemClick(itemName: ItemsEntity, itemId: String)
    }
}