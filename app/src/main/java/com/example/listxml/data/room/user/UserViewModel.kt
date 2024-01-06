package com.example.listxml.data.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _isUserLoggedInState = MutableLiveData(false)
    val isUserLoggedInState: LiveData<Boolean> = _isUserLoggedInState

    private val _shouldNavigate = MutableLiveData(false)
    val shouldNavigate: LiveData<Boolean> = _shouldNavigate

    private val _lists = MutableLiveData<List<ListEntity>>(emptyList())
    val lists: LiveData<List<ListEntity>> = _lists

    private val _userId = MutableLiveData<String?>(null)
    val userId: LiveData<String?> = _userId

    fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(userEntity)
            _isUserLoggedInState.value = true
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
                _isUserLoggedInState.value = true
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
        withContext(Dispatchers.Main) {
            _isUserLoggedInState.value = false
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
                _shouldNavigate.value = true

            } else if (loggingState()) {
                _shouldNavigate.value = true
            }
        }
    }

    suspend fun getUserDetails(): UserEntity? {
        return userRepository.getUserByLoggedInStatus()
    }

    fun getUser() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val user = userRepository.getUserByLoggedInStatus()
            val userId = user?.id
            userSessionManager.setUserId(userId ?: "")
            userSessionManager.currentUser = user
            userSessionManager.isUserLoggedIn.value = true
            withContext(Dispatchers.Main) {
                _userId.value = userId
            }
        }
    }

    private suspend fun loggingState(): Boolean {
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