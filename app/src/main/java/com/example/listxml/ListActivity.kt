package com.example.listxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListActivity : AppCompatActivity(), ListAdapter.ListItemClickListener {
    private lateinit var binding: ActivityListBinding
    private val listViewModel: ListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarList)

        lifecycleScope.launch {
            listViewModel.getListsByUserId().collect {
                val shoppingList = ArrayList(it)
                setupListToRecyclerView(shoppingList)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Lists"
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.toolbarList.setNavigationOnClickListener{
            binding.drawerLayout.open()

        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddList::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun setupListToRecyclerView(list: List<ListEntity>) {
        if (list.isNotEmpty()) {
            var id = ""
            binding.rvList.visibility = View.VISIBLE
            binding.textViewEmpty.visibility = View.GONE
            binding.rvList.layoutManager = LinearLayoutManager(this)
            for (item in list) {
                id = item.id
            }
            val adapter = ListAdapter(list, this@ListActivity, id)
            binding.rvList.adapter = adapter

        } else {
            binding.rvList.visibility = View.GONE
            binding.textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(listName: ListEntity, listId: String) {
        val intent = Intent(this, AddList::class.java)
        intent.putExtra("listId", listId)
        startActivity(intent)
    }

    override fun onItemMoreClick(listName: ListEntity, listId: String) {

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
}