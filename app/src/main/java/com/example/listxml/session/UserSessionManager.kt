package com.example.listxml.session

import androidx.compose.runtime.mutableStateOf
import com.example.listxml.data.room.user.UserEntity


class UserSessionManager {
    var currentUser: UserEntity? = null
    var isUserLoggedIn = mutableStateOf(
        false
    )

    fun getUserId(): String {
        return currentUser?.id ?: ""
    }

    fun setUserId(newUserId: String){
        currentUser?.id = newUserId
    }

    fun getUser(): UserEntity {
        return currentUser!!
    }

    fun setUserLoggedIn(loggedIn: Boolean){
        isUserLoggedIn.value = loggedIn
    }

    fun signout(){
        clearSession()
    }
    fun clearSession(){
        isUserLoggedIn.value = false
        currentUser = null
    }

}