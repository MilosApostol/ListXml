package com.example.listxml.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.ListAdapter
import com.example.listxml.R
import com.example.listxml.data.firebase.list.ListFireViewModel
import com.example.listxml.data.firebase.user.UserFireViewModel
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.databinding.ActivityListBinding
import com.example.listxml.databinding.HeaderNavigationDrawerBinding
import com.example.listxml.databinding.ItemRvBinding
import com.example.listxml.databinding.ListItemRvBinding
import com.example.listxml.utill.hasInternetConnection
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException


@AndroidEntryPoint
class ListActivity : BaseActivity<ActivityListBinding>(), ListAdapter.ListItemClickListener {
    private lateinit var itemsBinding: ListItemRvBinding
    private lateinit var headerBinding: HeaderNavigationDrawerBinding
    private val listFireViewModel: ListFireViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val userFireViewModel: UserFireViewModel by viewModels()
    private lateinit var listAdapter: ListAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentUser: UserEntity
    override fun getViewBinding() = ActivityListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemsBinding = ListItemRvBinding.inflate(layoutInflater)
        headerBinding = HeaderNavigationDrawerBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbarList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Lists"
        sharedPreferences = getSharedPreferences(getString(R.string.mypreferences), MODE_PRIVATE)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        userViewModel.userId.observe(this@ListActivity) { id ->
            listFireViewModel.readData()
        }

        userViewModel.user.observe(this@ListActivity) { user ->
            currentUser = user ?: return@observe

            val imagePath = currentUser.image

            headerBinding.textViewUserName.text = currentUser.userName

            // Set image
            if (File(imagePath).exists()) {
                val imageUri = Uri.fromFile(File(imagePath))

                lifecycleScope.launch {
                    try {
                        val inputStream = this@ListActivity.contentResolver.openInputStream(imageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        headerBinding.imageViewUser.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            } else {
                headerBinding.imageViewUser.setImageResource(R.drawable.ic_default_person)
            }

        }


        listFireViewModel.lists.observe(this@ListActivity) {
            setupListToRecyclerView(it)
        }
        binding.toolbarList.setNavigationOnClickListener {
            binding.drawerLayout.open()

        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            when (menuItem.itemId) {
                //not working
                R.id.text_view_user_name ->{
                    val intent = Intent(this@ListActivity, UserActivity::class.java )
                    startActivity(intent)
                }
                R.id.lists -> {
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                }

                R.id.add_list -> {
                    val intent = Intent(this, AddList::class.java)
                    startActivity(intent)
                }
                R.id.items -> {
                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                }

                R.id.add_items -> {
                    val intent = Intent(this, AddItems::class.java)
                    startActivity(intent)
                }
            }
            binding.drawerLayout.close()
            true
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                if (!hasInternetConnection()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        userFireViewModel.logOutOnline()
                    }
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        userViewModel.logOutOffline()
                    }
                }
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupListToRecyclerView(list: List<ListEntity>) {
        if (list.isNotEmpty()) {
            binding.rvList.visibility = View.VISIBLE
            binding.textViewEmpty.visibility = View.GONE
            binding.rvList.layoutManager = LinearLayoutManager(this)

            val listIds = list.map { it.id }
            listAdapter = ListAdapter(list, this@ListActivity, listIds)
            binding.rvList.adapter = listAdapter
        } else {
            binding.rvList.visibility = View.GONE
            binding.textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun onListItemCLick(listName: ListEntity, listId: String) {
        val intent = Intent(this, ItemsActivity::class.java)
        with(sharedPreferences.edit()) {
            putString(getString(R.string.listidPref), listId)
            apply()
        }
        startActivity(intent)
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
                        listFireViewModel.removeList(listId)
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
