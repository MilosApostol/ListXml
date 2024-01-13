package com.example.listxml.data.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    var userLoggedIn: Boolean = false,
    val userHolder: String = "",
    val image: String = "",

    ) {
}