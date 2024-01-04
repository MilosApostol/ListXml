package com.example.listxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register Screen"

        binding.buttonRegister.setOnClickListener {
            val name = binding.textViewName.text.toString()
            val email = binding.textViewEmail.text.toString()
            val password = binding.textViewPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = UserEntity(userName = name, email = email, password = password)
                userViewModel.insertUser(user)
                Toast.makeText(this@RegisterActivity, "added",
                    Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(this@RegisterActivity, "Fill all of the fields",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}