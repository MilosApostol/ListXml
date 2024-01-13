package com.example.listxml.data.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.firebase.user.UserFireViewModel
import com.example.listxml.data.firebase.user.UserRepFirebase
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userFireRepository: UserRepFirebase,
    val userSessionManager: UserSessionManager
) : ViewModel() {

    val isUserLoggedInState = MutableLiveData(false)

    val shouldNavigate = MutableLiveData(false)

    val lists = MutableLiveData<List<ListEntity>>(emptyList())

    val userId = MutableLiveData<String?>(null)


    init {
        getUser()
    }

    fun updateUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(userEntity)
        }
    }

    suspend fun getUserByEmailAndPasswordOffline(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.password == password) {
                withContext(Dispatchers.Main) {
                    isUserLoggedInState.value = true
                    userSessionManager.apply {
                        setUserLoggedIn(true)
                        currentUser = user
                    }
                }
                true
            } else {
                false
            }
        }
    }

    suspend fun logOutOffline() {
        val user: UserEntity? = userRepository.getUserByLoggedInStatus()
        user?.userLoggedIn = false
        withContext(Dispatchers.Main) {
            isUserLoggedInState.value = false
        }
        if (user != null) {
            userRepository.updateUser(user)
            userSessionManager.apply {
                setUserLoggedIn(false)
                currentUser = null
            }
        }
    }

    fun checkConditions() {
        viewModelScope.launch {
            if (isUserLoggedInState.value == true) {
                shouldNavigate.value = true
            } else if (loggingStateOffline()) {
                shouldNavigate.value = true
            } else if (!loggingStateOffline()) {
                Firebase.auth.signOut()
                shouldNavigate.value = false
            }
        }
    }

    private fun getUser() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val user = userRepository.getUserByLoggedInStatus()
            if (user != null) {
                userSessionManager.currentUser = user
                userSessionManager.isUserLoggedIn.value = true
                withContext(Dispatchers.Main) {
                    userId.value = user.id
                }
                if (user.id != Firebase.auth.currentUser?.uid.toString()) {
                    withContext(Dispatchers.IO) {
                        updateRoomUserIdAfterLogin(Firebase.auth.currentUser?.email.toString())
                    } //setting a roomID == firebaseID
                }
            }
        }
    }

    suspend fun getUserDetails(): UserEntity? {
        return userRepository.getUserByLoggedInStatus()
    }

    suspend fun logInAfterOffline(email: String, password: String) {
        isUserLoggedInState.value = true
        userFireRepository.logIn(email, password)
    }

    private suspend fun updateRoomUserIdAfterLogin(email: String): Boolean {
        return userRepository.updateRoomUserIdAfterLogin(email)
    }

    private suspend fun loggingStateOffline(): Boolean {
        return withContext(Dispatchers.IO) {
            val loggedInUser = userRepository.getUserByLoggedInStatus()
            if (loggedInUser != null) {
                userSessionManager.currentUser = loggedInUser
                userSessionManager.isUserLoggedIn.value = true
                true
            } else {
                false
            }
        }
    }
}