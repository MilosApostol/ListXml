package com.example.listxml.data.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val userLoggedIn: Boolean = false
) {
}