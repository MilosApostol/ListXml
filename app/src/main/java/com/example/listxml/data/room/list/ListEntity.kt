package com.example.listxml.data.room.list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "list_table")
class ListEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    @ColumnInfo(name = "listName")
    val name: String = "",
    @ColumnInfo(name = "userId")
    val listCreatorId: String = "",
)