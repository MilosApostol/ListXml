package com.example.listxml.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.ChooseItemsAdapter
import com.example.listxml.additems.AddItemsEntity
import com.example.listxml.additems.AddItemsViewModel
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.data.room.item.ItemsViewModel
import com.example.listxml.databinding.ActivityAddItemsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AddItems : AppCompatActivity(), ChooseItemsAdapter.ChooseItemClickListener {
    lateinit var binding: ActivityAddItemsBinding
    private val addItemsViewModel: AddItemsViewModel by viewModels()
    private val itemsViewModel: ItemsViewModel by viewModels()
    private lateinit var addItemsAdapter: ChooseItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

            addItemsAdapter = ChooseItemsAdapter().apply {
                onItemClicked = { item, id ->
                    onAddItem(
                        ItemsEntity(
                            UUID.randomUUID().toString(),
                            item.title,
                            item.description,
                            item.price
                        )
                    )
                    val intent = Intent(this@AddItems, ItemsActivity::class.java)
                    startActivity(intent)

                }
            }
        binding.rvAddItems.adapter = addItemsAdapter
        binding.rvAddItems.visibility = View.GONE


        addItemsAdapter.filter(binding.etSearch.editText?.text ?: "")

        binding.etSearch.editText?.doOnTextChanged { text, _, _, _ ->
            binding.rvAddItems.visibility = View.VISIBLE
            text?.let {
                addItemsAdapter.filter(it)
            }
        }


        binding.rvAddItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addItemsAdapter
        }

        addItemsViewModel.allItemsData.observe(this) {
            addItemsAdapter.submitItems(it)
        }


    }

    private fun onAddItem(item: ItemsEntity) {
        itemsViewModel.insertItems(item)
    }

    override fun onItemClick(itemName: AddItemsEntity, itemId: String) {

    }
}

/*
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.search, menu)
            val searchItem = menu?.findItem(R.id.action_search)
            val searchView = searchItem?.actionView as? SearchView ?: return false

            searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        //     filter(newText);
                        //    val filteredData = originalData.filter { item ->
                        //        item.name.contains(newText, ignoreCase = true) // Adjust filtering criteria
                        //         adapter.submitList(filteredData) // Update adapter with filtered data
                    } else {
                        //          adapter.submitList(originalData) // Reset to original data if query is empty
                    }
                    return true

                }
            }
            )
            return true
        }


        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_logout -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                    }
                    val intent = Intent(this, LoginScreen::class.java)
                    startActivity(intent)
                    return true
                }

                else -> return super.onOptionsItemSelected(item)
            }
        }
    }

 */