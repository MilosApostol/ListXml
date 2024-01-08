
package com.example.listxml.data.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.listxml.data.room.item.ItemsDao
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.data.room.list.ListDao
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.user.UserDao
import com.example.listxml.data.room.user.UserEntity

@Database(entities = [UserEntity::class, ListEntity::class, ItemsEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun listDao(): ListDao

    abstract fun itemsDao(): ItemsDao

    companion object {
        val DATABASE = "database"
    }


}