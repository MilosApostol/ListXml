package com.example.listxml.data.room.user

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val dao: UserDao) {

    fun insertUser(userEntity: UserEntity){
        dao.insertUser(userEntity)
    }

    fun updateUser(userEntity: UserEntity){
        dao.updateUser(userEntity)
    }

    fun getUserByEmail(email: String): UserEntity{
        return dao.getUserByName(email)
    }

    fun userExist(email: String): Boolean{
        return dao.userExists(email)
    }

    suspend fun getUserByLoggedInStatus(): UserEntity? {
        return withContext(Dispatchers.IO) {
            dao.getUserByLoggedInStatus(true)
        }
    }

    suspend fun updateRoomUserIdAfterLogin(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val existingUser = dao.getUserByName(username)

                dao.updateUserId(existingUser.id, firebaseUserId)
                true
            } catch (e: Exception) {
                false
            }
        }

    }
}


