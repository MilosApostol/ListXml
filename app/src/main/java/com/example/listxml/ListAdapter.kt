package com.example.listxml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.databinding.ItemRvBinding
import com.example.listxml.databinding.ListItemRvBinding

class ListAdapter(
    val items: List<ListEntity>,
    private var itemClickListener: ListItemClickListener,
    private var listIds: List<String>
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ListEntity
        ) {
            binding.textName.text = item.name
            binding.itemMore.setImageResource(R.drawable.ic_dots)

            binding.itemMore.setOnClickListener {
                itemClickListener.onItemMoreClick(item, listIds[bindingAdapterPosition], itemView)
            }

            binding.root.setOnClickListener {
                itemClickListener.onListItemCLick(item, listIds[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemRvBinding.inflate(inflater, parent, false)
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
        fun onItemMoreClick(listName: ListEntity, listId: String, anchorView: View){
        }
    }

}
