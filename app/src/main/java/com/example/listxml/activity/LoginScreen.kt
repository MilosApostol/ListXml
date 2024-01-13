package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.firebase.user.UserFireViewModel
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.databinding.ActivityLoginScreenBinding
import com.example.listxml.utill.hasInternetConnection
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginScreen : BaseActivity<ActivityLoginScreenBinding>() {

    private val userViewModel: UserViewModel by viewModels()
    private val userFireViewModel: UserFireViewModel by viewModels()
    override fun getViewBinding(): ActivityLoginScreenBinding {
        return ActivityLoginScreenBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    if (hasInternetConnection()) {
                        lifecycleScope.launch {
                            if (userFireViewModel.logIn(email, password)) {
                                withContext(Dispatchers.Main) {
                                    val intent = Intent(this@LoginScreen, ListActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    } else {
                        lifecycleScope.launch {
                            if (userViewModel.getUserByEmailAndPasswordOffline(
                                    email,
                                    password
                                )
                            ) {
                                Toast.makeText(this@LoginScreen, "true", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
            binding.textViewSignup.setOnClickListener {
                if (hasInternetConnection()) {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginScreen, "You can not register without network",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }
    }
}