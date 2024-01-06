package com.example.listxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityAddListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class AddList : AppCompatActivity() {
    private lateinit var binding: ActivityAddListBinding
    private val listViewModel: ListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val userId = intent.getStringExtra("userId")
        val listId = intent.getStringExtra("listId")
        if (listId != null) {
            //this works
            lifecycleScope.launch(Dispatchers.Main) {
                listViewModel.getListById(listId)
                listViewModel.listUpdate.observe(this@AddList) { list ->
                    when {
                        list != null && list.id == listId -> binding.textViewList.setText(list.name)
                        else -> {
                        }
                    }
                }
            }
        }

        binding.buttonAddList.setOnClickListener {
            val listName = binding.textViewList.text.toString()
            if (listName.isNotEmpty()) {
                if (listId != null && userId != null) {
                    listViewModel.updateList(
                        ListEntity(
                            id = listId,
                            name = listName,
                            listCreatorId = userId
                        )
                    )
                    val intent = Intent(this@AddList, ListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "sad insert", Toast.LENGTH_LONG).show()
                    listViewModel.insertLists(
                        ListEntity(
                            name = listName,
                            id = UUID.randomUUID().toString(),
                            listCreatorId = userId ?: ""
                        )
                    )
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this@AddList, "Empty list", Toast.LENGTH_LONG).show()
            }
        }
    }
}