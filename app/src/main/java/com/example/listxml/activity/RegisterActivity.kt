package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.firebase.UserFireViewModel
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
    private val userFireViewModel: UserFireViewModel by viewModels()

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

            binding.progressBar.visibility = View.VISIBLE
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    if (userFireViewModel.signIn(
                            name = name,
                            email = email,
                            password = password,
                        )
                    ) {
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(this@RegisterActivity, ListActivity::class.java)
                        startActivity(intent)
                    }
                    /*
                //if offline
                lifecycleScope.launch {
                    if (userViewModel.getUserByEmail(email)) {
                        val user = UserEntity(
                            id = UUID.randomUUID().toString(),
                            userName = name,
                            email = email,
                            password = password,
                            userLoggedIn = true
                        )
                        userViewModel.insertUserOffline(user)
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

                 */
                }
            }
        }
    }
}