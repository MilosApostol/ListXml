package com.example.listxml.data.room.list

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "list_table")
class ListEntity(
    val id: String = "",

    @ColumnInfo(name = "listName")
    val name: String = "",
    @ColumnInfo(name = "userId")
    val listCreatorId: String = "",
)