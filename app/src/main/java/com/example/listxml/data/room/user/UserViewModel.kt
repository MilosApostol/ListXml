package com.example.listxml.data.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.data.room.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(userEntity)
        }
    }

    fun updateUser(userEntity: UserEntity) {
        viewModelScope.launch {
            userRepository.updateUser(userEntity)
        }
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                user.password == password
            } else {
                false
            }
        }
    }
}