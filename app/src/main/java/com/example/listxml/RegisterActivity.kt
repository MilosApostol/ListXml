package com.example.listxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID


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
                lifecycleScope.launch {
                    if (userViewModel.getUserByEmail(email)) {
                        val user = UserEntity(
                            userName = name,
                            email = email,
                            password = password,
                            userLoggedIn = true
                        )
                        userViewModel.insertUser(user)
                        val intent = Intent(this@RegisterActivity, ListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@RegisterActivity, "User Alreday exist",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            } else {
                Toast.makeText(
                    this@RegisterActivity, "Fill all of the fields",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }
}
/*
list nepokretnosti da li je na objektu ili na parceli ima datum upisa u korist
        IMa G list tu j
        teret na objektu

       teret na objektu i dole pise PR primljeno to i to upisano pod DN broj kroz 64 na kraju pise u korst  011 3338317 boojajna popvivc

 */