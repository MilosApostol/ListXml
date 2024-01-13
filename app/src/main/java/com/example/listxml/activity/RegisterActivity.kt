package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.firebase.user.UserFireViewModel
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


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
                    try {
                        withTimeout(7000) {
                            if (userFireViewModel.signIn(
                                    name = name,
                                    email = email,
                                    password = password,
                                )
                            ) {
                                binding.progressBar.visibility = View.GONE
                                val intent = Intent(this@RegisterActivity, ListActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Registration failed
                                throw Exception("Registration failed")
                            }
                        }
                    } catch (e: TimeoutCancellationException) {
                        // Timeout occurred
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@RegisterActivity,
                            "Operation timed out",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        // Other exceptions
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "please fill all the filds", Toast.LENGTH_SHORT).show()
            }

        }

    }
}

