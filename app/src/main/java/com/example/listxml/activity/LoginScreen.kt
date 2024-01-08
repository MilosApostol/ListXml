package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.firebase.UserFireViewModel
import com.example.listxml.data.firebase.UserRepFirebase
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.databinding.ActivityLoginScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val userFireViewModel: UserFireViewModel by viewModels()
    private var isUserLogged = false
    private lateinit var binding: ActivityLoginScreenBinding
    private var user: UserEntity? = null
    private lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        userViewModel.checkConditions()
        userViewModel.shouldNavigate.observe(this) {
            if (it) {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.buttonLogin.setOnClickListener {
                val email = binding.textViewEmailLogin.text.toString()
                val password = binding.textViewPasswordLogin.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    lifecycleScope.launch {
                        if (userFireViewModel.logIn(email, password)) {
                            val intent = Intent(this@LoginScreen, ListActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

            /*
                // if offline
                        lifecycleScope.launch {
                            if (userViewModel.getUserByEmailAndPasswordOffline(
                                    email,
                                    password
                                )
                            ) {
                                Toast.makeText(this@LoginScreen, "true", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                     */
            binding.textViewSignup.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}