package com.example.listxml.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.ChooseItemsAdapter
import com.example.listxml.Constants
import com.example.listxml.R
import com.example.listxml.additems.AddItemsEntity
import com.example.listxml.additems.AddItemsViewModel
import com.example.listxml.data.firebase.items.ItemsFireViewModel
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.data.room.item.ItemsViewModel
import com.example.listxml.databinding.ActivityAddItemsBinding
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class AddItems : AppCompatActivity(), ChooseItemsAdapter.ItemClickListener {
    lateinit var binding: ActivityAddItemsBinding
    private val addItemsViewModel: AddItemsViewModel by viewModels()
    private val itemsViewModel: ItemsViewModel by viewModels()
    private val itemsFireViewModel: ItemsFireViewModel by viewModels()
    private lateinit var addItemsAdapter: ChooseItemsAdapter
    private lateinit var id: String
    private val reference = FirebaseDatabase.getInstance().getReference(Constants.Items)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addItemsViewModel.allItemsData.observe(this) { itemsList ->
            setupListToRecyclerView(itemsList)
            binding.rvAddItems.visibility = View.GONE
        }
        val sharedPreferences = getSharedPreferences(getString(R.string.mypreferences), MODE_PRIVATE)
        id = sharedPreferences.getString(getString(R.string.listidPref), "") ?: ""

    }


    private fun setupListToRecyclerView(items: List<AddItemsEntity>) {
        if (items.isNotEmpty()) {
            val listIds = items.map { it.id }
            addItemsAdapter = ChooseItemsAdapter(items, listIds)
            binding.rvAddItems.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addItemsAdapter
            }

            binding.etSearch.editText?.setOnClickListener {
                handleEditTextFocus()
            }

            binding.etSearch.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    handleEditTextFocus()
                }
            }

            binding.etSearch.editText?.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    addItemsAdapter.filter(it)
                }
            }
            //      addItemsAdapter.onItemClicked = { item, id ->

            addItemsAdapter.submitItems(items)
            addItemsAdapter.addItemClickListener(this)
            binding.rvAddItems.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addItemsAdapter
            }
        }
    }
    private fun handleEditTextFocus() {
        addItemsAdapter.filter(binding.etSearch.editText?.text ?: "")
        binding.rvAddItems.visibility = View.VISIBLE
    }
    override fun onItemClick(item: AddItemsEntity) {
        val reference = FirebaseDatabase.getInstance().getReference(Constants.Items)
        val key = reference.key!!
        val item = ItemsEntity(
                UUID.randomUUID().toString(),
                item.title,
                item.description,
                id
            )
            reference.push()
                .setValue(item){_, ref ->
                val key = ref.key
                item.id = key!!


                lifecycleScope.launch {
                    itemsFireViewModel.insertItems(reference, item, key) { _ ->
                        val intent = Intent(this@AddItems, ItemsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

            }
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