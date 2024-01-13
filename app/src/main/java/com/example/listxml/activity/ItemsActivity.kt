package com.example.listxml.activity

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.ItemsAdapter
import com.example.listxml.R
import com.example.listxml.data.firebase.items.ItemsFireViewModel
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.databinding.ActivityItemsBinding
import com.example.listxml.utill.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsActivity : BaseActivity<ActivityItemsBinding>(), ItemsAdapter.ItemClickListener {
    val itemsFireViewModel: ItemsFireViewModel by viewModels()
    private lateinit var adapter: ItemsAdapter
    private var filteredLists: List<ItemsEntity> = emptyList()
    override fun getViewBinding() = ActivityItemsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarItems)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Items"

        val sharedPreferences =
            getSharedPreferences(getString(R.string.mypreferences), MODE_PRIVATE)
        val id = sharedPreferences.getString(getString(R.string.listidPref), "") ?: ""
        itemsFireViewModel.fetchItems()
        itemsFireViewModel.items.observe(this@ItemsActivity) {

            filteredLists = it.filter { items -> items.listParent == id }
            val listIds = filteredLists.map { it.id }
            adapter = ItemsAdapter(filteredLists, listIds, this@ItemsActivity)
            binding.recyclerViewItems.adapter = adapter
            setupItemsToRecyclerView(filteredLists)
        }

        binding.fabItems.setOnClickListener {
            val intent = Intent(this, AddItems::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    override fun onNetworkAvailable(available: Boolean) {
        super.onNetworkAvailable(available)
    }

    private fun setupItemsToRecyclerView(items: List<ItemsEntity>) {
        if (items.isNotEmpty()) {
            binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
            val swipeHandler = object : SwipeToDeleteCallback() {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem = menu?.findItem(R.id.search)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        val component = ComponentName(this, ItemsActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredData = filteredLists.filter {
                        it.name.contains(newText, ignoreCase = true)
                    }
                    adapter.submitList(filteredData)
                } else {
                    adapter.submitList(filteredLists)
                }
                return true
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                return true
            }

            android.R.id.home -> {
                val intent = Intent(this@ItemsActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}