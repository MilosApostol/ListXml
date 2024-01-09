package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.listxml.ItemsAdapter
import com.example.listxml.R
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.databinding.ActivityItemsBinding

class ItemsActivity : AppCompatActivity(), ItemsAdapter.ItemClickListener {
    private lateinit var binding: ActivityItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fabItems.setOnClickListener {
            val intent = Intent(this, AddItems::class.java)
            startActivity(intent)
        }

    }

    override fun onItemClick(itemName: ItemsEntity, itemId: String) {
        TODO("Not yet implemented")
    }
}