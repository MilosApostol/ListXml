package com.example.listxml.data.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val userSessionManager: UserSessionManager
) : ViewModel() {

    fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(userEntity)
            userSessionManager.apply {
                setUserLoggedIn(true)
                currentUser = userEntity
            }

        }
    }

    fun updateUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(userEntity)
        }
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.password == password) {
                userSessionManager.apply {
                    setUserLoggedIn(true)
                    currentUser = user
                }
                true
            } else {
                false
            }
        }
    }

    suspend fun getUserByEmail(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByEmail(email)
            if (user == null) {
                true
            } else {
                false
            }
        }
    }

    suspend fun logOut() {
        val user: UserEntity? = userRepository.getUserByLoggedInStatus()
        user?.userLoggedIn = false
        if (user != null) {
            userRepository.updateUser(user)
            userSessionManager.apply {
                setUserLoggedIn(false)
                currentUser = null
            }
        }
    }

    suspend fun getLoggedUser(): UserEntity {
        val user = userRepository.getUserByLoggedInStatus()
        return user ?: UserEntity()
    }
}