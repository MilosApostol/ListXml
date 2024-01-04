package com.example.listxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityAddListBinding

class AddList : AppCompatActivity() {
    private lateinit var binding: ActivityAddListBinding
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonAddList.setOnClickListener {
            val listName = binding.textViewList.text.toString()
            if (listName.isNotEmpty()) {
                listViewModel.insertLists(ListEntity(listName))
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@AddList, "Empty list", Toast.LENGTH_LONG).show()
            }
        }
    }
}