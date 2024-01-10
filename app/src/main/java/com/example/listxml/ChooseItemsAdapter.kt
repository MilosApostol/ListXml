package com.example.listxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.additems.AddItemsEntity
import com.example.listxml.databinding.ChooseItemsBinding


class ChooseItemsAdapter() :
    RecyclerView.Adapter<ChooseItemsAdapter.ViewHolder>() {
    private var itemsList = listOf<AddItemsEntity>()
    private var filteredItemsList = emptyList<AddItemsEntity>()
    private var alreadyAddedItem = listOf<AddItemsEntity>()
    var onItemClicked: (AddItemsEntity, String) -> Unit = { _, _ -> }
    private lateinit var listIds: List<String>



    inner class ViewHolder(binding: ChooseItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.textViewItem
        private val description = binding.textViewDescription
        private val price = binding.textViewPrice

        fun bind(addItemsEntity: AddItemsEntity) {
            title.text = addItemsEntity.title
            description.text = addItemsEntity.description
            price.text = addItemsEntity.price

            description.maxLines = 2

            itemView.setOnClickListener {
                onItemClicked(addItemsEntity, listIds[bindingAdapterPosition])
            }

            if (alreadyAddedItem.any { it.id == addItemsEntity.id }) {
                description.alpha = 0.5f
                price.alpha = 0.5f
                itemView.isClickable = false
            } else {
                description.alpha = 1.0f
                price.alpha = 1.0f
                itemView.isClickable = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChooseItemsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun submitItems(list: List<AddItemsEntity>) {
        itemsList = list
        filteredItemsList = itemsList
        notifyDataSetChanged()
    }

    fun submitAddedItems(list: List<AddItemsEntity>) {
        alreadyAddedItem = list
        notifyDataSetChanged()
    }

    fun filter(charSequence: CharSequence) {
        filteredItemsList = if (charSequence.isEmpty()) {
            itemsList
        } else {
            itemsList.filter {
                it.title.contains(
                    charSequence, true
                )
            }
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return filteredItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItemsList[position])
    }

    interface ChooseItemClickListener{
        fun onItemClick(itemName: AddItemsEntity, itemId: String)
    }
}