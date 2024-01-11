package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.ItemsAdapter
import com.example.listxml.ListAdapter
import com.example.listxml.R
import com.example.listxml.data.firebase.items.ItemsFireViewModel
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.databinding.ActivityItemsBinding
import com.example.listxml.utill.ItemTouchHelperCallback
import com.example.listxml.utill.SwipeToDeleteCallback
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsActivity : AppCompatActivity(), ItemsAdapter.ItemClickListener {
    private lateinit var binding: ActivityItemsBinding
    val itemsFireViewModel: ItemsFireViewModel by viewModels()
    private lateinit var adapter: ItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val sharedPreferences = getSharedPreferences(getString(R.string.mypreferences), MODE_PRIVATE)
        val id = sharedPreferences.getString(getString(R.string.listidPref), "") ?: ""
      itemsFireViewModel.fetchItems()
        itemsFireViewModel.items.observe(this@ItemsActivity) {

            val filteredLists = it.filter { items -> items.listParent == id }

            setupItemsToRecyclerView(filteredLists)
        }

        binding.fabItems.setOnClickListener {
            val intent = Intent(this, AddItems::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun setupItemsToRecyclerView(items: List<ItemsEntity>) {
        if (items.isNotEmpty()) {
            binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
            val listIds = items.map { it.id }
            adapter = ItemsAdapter(items, listIds, this@ItemsActivity)
            binding.recyclerViewItems.adapter = adapter
            val swipeHandler = object : SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.bindingAdapterPosition
                        val itemToDelete = adapter.getItemsId(position)
                        val itemId = itemToDelete.id
                        Toast.makeText(this@ItemsActivity, itemId, Toast.LENGTH_SHORT).show()
                        adapter.deleteItem(position)
                        itemsFireViewModel.removeItem(itemId)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding.recyclerViewItems)
        }
    }

    override fun onItemClick(itemName: ItemsEntity, itemId: String) {
       Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
    }


}