package com.example.listxml.data.room.list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "list_table")
data class ListEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    @ColumnInfo(name = "listName")
    var name: String = "",
    @ColumnInfo(name = "userId")
    var listCreatorId: String = "",
)