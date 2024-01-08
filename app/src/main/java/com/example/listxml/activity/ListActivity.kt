package com.example.listxml.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.ListAdapter
import com.example.listxml.R
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityListBinding
import com.example.listxml.databinding.ItemRvBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListActivity : AppCompatActivity(), ListAdapter.ListItemClickListener {
    private lateinit var binding: ActivityListBinding
    private lateinit var itemsBinding: ItemRvBinding
    private val listViewModel: ListViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var userId: String? = ""
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        itemsBinding = ItemRvBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Lists"

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        userViewModel.getUser()
        listViewModel.getListsByUserId()
        listViewModel.lists.observe(this@ListActivity) {
            setupListToRecyclerView(it)
        }
        userViewModel.userId.observe(this@ListActivity) { id ->
            userId = id
        }

        binding.toolbarList.setNavigationOnClickListener {
            binding.drawerLayout.open()

        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddList::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    userViewModel.logOutOffline()
                }
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupListToRecyclerView(list: List<ListEntity>) {
        if (list.isNotEmpty()) {
            binding.rvList.visibility = View.VISIBLE
            binding.textViewEmpty.visibility = View.GONE
            binding.rvList.layoutManager = LinearLayoutManager(this)

            // Create the adapter with a list of listIds, one for each item
            val listIds = list.map { it.id }
            listAdapter = ListAdapter(list, this@ListActivity, listIds)
            binding.rvList.adapter = listAdapter
        } else {
            binding.rvList.visibility = View.GONE
            binding.textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun onListItemCLick(listName: ListEntity, listId: String) {
        Toast.makeText(this@ListActivity, "diffrence is this $listId", Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(listName: ListEntity, listId: String) {
        Toast.makeText(this@ListActivity, listId, Toast.LENGTH_LONG).show()

    }

    override fun onItemMoreClick(listName: ListEntity, listId: String, anchorView: View) {
        showPopupMenu(anchorView, listId)
    }

    private fun showPopupMenu(anchorView: View, listId: String) {
        val popupMenu = PopupMenu(this@ListActivity, anchorView)
        popupMenu.apply {
            gravity = Gravity.BOTTOM or Gravity.START
            menuInflater.inflate(R.menu.list_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        listViewModel.removeList(listId)
                    }

                    R.id.action_rename -> {
                        val intent = Intent(this@ListActivity, AddList::class.java)
                        intent.putExtra(getString(R.string.listid), listId)
                        intent.putExtra(getString(R.string.userid), listId)

                        startActivity(intent)
                    }
                }
                true
            }
            show()
        }
    }
}
