package com.example.listxml.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
class AddItems : BaseActivity<ActivityAddItemsBinding>(), ChooseItemsAdapter.ItemClickListener {
    override fun getViewBinding() = ActivityAddItemsBinding.inflate(layoutInflater)

    private val addItemsViewModel: AddItemsViewModel by viewModels()
    private val itemsFireViewModel: ItemsFireViewModel by viewModels()
    private lateinit var addItemsAdapter: ChooseItemsAdapter
    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "AddItems"

        addItemsViewModel.allItemsData.observe(this) { itemsList ->
            setupListToRecyclerView(itemsList)
            binding.rvAddItems.visibility = View.GONE
        }
        val sharedPreferences = getSharedPreferences(getString(R.string.mypreferences), MODE_PRIVATE)
        id = sharedPreferences.getString(getString(R.string.listidPref), "") ?: ""

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
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
                handleEditTextFocus(items)
            }

            binding.etSearch.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    handleEditTextFocus(items)
                }
            }

            binding.etSearch.editText?.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    addItemsAdapter.filter(it)
                }
            }
            addItemsAdapter.addItemClickListener(this)
            binding.rvAddItems.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = addItemsAdapter
            }
        }
    }
    private fun handleEditTextFocus(items: List<AddItemsEntity>) {
        val searchText = binding.etSearch.editText?.text.toString().trim().lowercase()

        // Filter and remove duplicates:
        val filteredList = addItemsAdapter.items.filter { item ->
            item.title.trim().lowercase().contains(searchText)
        }.distinctBy { it.title.trim().lowercase() }

        addItemsAdapter.submitItems(filteredList) // Update adapter with filtered list
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