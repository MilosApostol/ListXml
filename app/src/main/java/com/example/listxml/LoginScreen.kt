package com.example.listxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.databinding.ActivityLoginScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var binding: ActivityLoginScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonLogin.setOnClickListener {
            val email = binding.textViewEmailLogin.text.toString()
            val password = binding.textViewPasswordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    if (userViewModel.getUserByEmailAndPassword(
                            email,
                            password
                        )
                    ) {
                        Toast.makeText(this@LoginScreen, "true", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.textViewSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


}