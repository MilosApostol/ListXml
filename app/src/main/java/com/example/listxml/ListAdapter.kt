package com.example.listxml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.databinding.ItemRvBinding

class ListAdapter(
    val items: List<ListEntity>,
    private var itemClickListener: ListItemClickListener, private var listId: String
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ListEntity
        ) {
            binding.textName.text = item.name
            binding.itemMore.setImageResource(R.drawable.ic_dots)

            binding.textName.setOnClickListener {
                itemClickListener.onItemClick(item, listId)
            }
            binding.itemMore.setOnClickListener {
                itemClickListener.onItemMoreClick(item, listId)
            }

            binding.root.setOnClickListener {
                itemClickListener.onListItemCLick(item, listId)
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

    interface ListItemClickListener {
        fun onListItemCLick(listName: ListEntity, listId: String)
        fun onItemClick(listName: ListEntity, listId: String)
        fun onItemMoreClick(listName: ListEntity, listId: String)
    }

}
