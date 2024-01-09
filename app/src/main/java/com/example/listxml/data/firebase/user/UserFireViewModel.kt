package com.example.listxml.data.firebase.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserFireViewModel @Inject constructor(
    val repository: UserRepFirebase,
    val userRepository: UserRepository,
    val userSessionManager: UserSessionManager,
    val auth: FirebaseAuth
) : ViewModel() {

    private val isUserLoggedInState = MutableLiveData(false)

    suspend fun logIn(email: String, password: String): Boolean {
        return withContext(Dispatchers.Main) {
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                if (user.password == password) {
                    userSessionManager.currentUser = user
                    userRepository.updateUser(user.copy(userLoggedIn = true))
                    isUserLoggedInState.value = true
                    return@withContext repository.logIn(email, password)
                } else {
                    return@withContext false
                }
            } else {
                return@withContext false
            }
        }
    }

    suspend fun signIn(name: String, email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.userExist(email)
            if (!user) {
                if (repository.signUp(email, password)) {
                    val newUser = UserEntity(
                        id = UUID.randomUUID().toString(),
                        userName = name,
                        email = email,
                        password = password,
                        userLoggedIn = true,
                    )
                    withContext(Dispatchers.Main) {
                        isUserLoggedInState.value = true
                        Firebase.auth.currentUser?.uid?.let { userSessionManager.setUserId(it) }
                        userSessionManager.currentUser = newUser
                    }
                    userRepository.insertUser(newUser)

                    return@withContext true
                } else {
                    return@withContext false
                }
            } else {
                return@withContext false
            }
        }
    }
}