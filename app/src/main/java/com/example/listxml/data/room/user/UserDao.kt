package com.example.listxml.data.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: UserEntity)

    @Update
    abstract fun updateUser(user: UserEntity)

    @Query("SELECT * FROM `user_table` WHERE email = :email")
    abstract fun getUserByName(email: String): UserEntity

    @Query("SELECT EXISTS(SELECT 1 FROM `user_table` WHERE email = :email)")
    abstract fun userExists(email: String): Boolean
    @Query("SELECT * FROM user_table WHERE userLoggedIn = :isLoggedIn LIMIT 1")
    abstract fun getUserByLoggedInStatus(isLoggedIn: Boolean): UserEntity?
}