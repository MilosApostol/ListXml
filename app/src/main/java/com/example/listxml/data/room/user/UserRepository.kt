package com.example.listxml.data.room.user

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
}