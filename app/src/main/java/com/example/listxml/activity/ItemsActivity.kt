package com.example.listxml.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.listxml.R
import com.example.listxml.databinding.ActivityItemsBinding

class ItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}