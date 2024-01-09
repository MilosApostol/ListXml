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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.ChooseItemsAdapter
import com.example.listxml.ListAdapter
import com.example.listxml.R
import com.example.listxml.additems.AddItemsEntity
import com.example.listxml.additems.AddItemsViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.databinding.ActivityAddItemsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class AddItems : AppCompatActivity() {
    lateinit var binding: ActivityAddItemsBinding
    val addItemsViewModel: AddItemsViewModel by viewModels()
    private lateinit var addItemsAdapter: ChooseItemsAdapter
    var courseModelArrayList = ArrayList<AddItemsEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        addItemsAdapter = ChooseItemsAdapter()
        binding.rvAddItems.adapter = addItemsAdapter
        lifecycleScope.launch {
            val items = addItemsViewModel.getItems()
            withContext(Dispatchers.IO) {
                addItemsAdapter.submitList(items)
            }
        }
        buildRecyclerView()


        addItemsAdapter = ChooseItemsAdapter().apply {
            onItemClicked = {
                onAddCoin(it)
                dismiss()
            }
        }


    }


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
                    filter(newText);
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

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<AddItemsEntity> = ArrayList<AddItemsEntity>()

        // running a for loop to compare elements.
        for (item in courseModelArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.title.toString().toLowerCase(Locale.ROOT)
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            addItemsAdapter.filterList(filteredlist)
        }
    }


    fun buildRecyclerView() {
        addItemsAdapter = ChooseItemsAdapter()
        binding.rvAddItems.adapter = addItemsAdapter
    }
}