package com.example.listxml.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.ChooseItemsAdapter
import com.example.listxml.ListAdapter
import com.example.listxml.R
import com.example.listxml.additems.AddItemsEntity
import com.example.listxml.additems.AddItemsViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.databinding.ActivityAddItemsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@AndroidEntryPoint
class AddItems : AppCompatActivity() {
    lateinit var binding: ActivityAddItemsBinding
    private val addItemsViewModel: AddItemsViewModel by viewModels()
    private lateinit var addItemsAdapter: ChooseItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        addItemsAdapter = ChooseItemsAdapter()
        binding.rvAddItems.adapter = addItemsAdapter


        addItemsAdapter = ChooseItemsAdapter().apply {
            onItemClicked = {
                onItemClicked(it)
                Toast.makeText(this@AddItems, "click", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etSearch.editText?.doOnTextChanged { text, _, _, _ ->
            text?.let {
                addItemsAdapter.filter(it)
            }
        }

        binding.rvAddItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addItemsAdapter
        }
        /*
        addItemsViewModel.addItems.observe(this) { items ->
            addItemsAdapter.submitItems(items)
        }

         */
        addItemsViewModel.allItemsData.observe(this){
            addItemsAdapter.submitItems(it ?: emptyList())
        }

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