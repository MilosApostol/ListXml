package com.example.listxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityAddListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class AddList : AppCompatActivity() {
    private lateinit var binding: ActivityAddListBinding
    private val listViewModel: ListViewModel by viewModels()
    private lateinit var list: ListEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val id = intent.getStringExtra("listId")
        if (id != null) {
            lifecycleScope.launch {
                listViewModel.getListById(id).collect {
                    list = it
                }
            }
        }
        binding.textViewList.setText(list.name)
        binding.buttonAddList.setOnClickListener {
            val listName = binding.textViewList.text.toString()

            if (listName.isNotEmpty()) {
                if (id != null) {
                    listViewModel.updateLists(ListEntity(id = id, name = listName))
                } else {
                    listViewModel.insertLists(
                        ListEntity(
                            name = listName,
                            id = UUID.randomUUID().toString(),
                            listCreatorId = id ?: ""
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