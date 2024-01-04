package com.example.listxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListActivity : AppCompatActivity(), ListAdapter.ListItemClickListener {
    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: ListAdapter
    private lateinit var recyclerView: RecyclerView
    private val listViewModel: ListViewModel by viewModels()
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lists"
        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        val viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
        viewModel.getAllLists.observe(this) { lists ->
            adapter.updateItems(lists) // Update the adapter with the latest lists
        }

        val adapter = ListAdapter(listItems, ::onListItemClick, this)

    }

    private fun handleImageClick(clickedView: View) {
        PopupMenu(this, clickedView).apply {
            menuInflater.inflate(R.menu.list_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        Toast.makeText(this@ListActivity, "this", Toast.LENGTH_LONG).show()
                     //   deleteItem(item) // Example function
                    }
                    R.id.action_rename -> {
                        // Perform rename action using the clicked item
                     //   renameItem(item) // Example function
                    }
                }
                true
            }
        }.show()
        }

    override fun onListItemCLick(listName: ListEntity, listId: String) {
        TODO("Not yet implemented")
    }
}