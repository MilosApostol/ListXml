package com.example.listxml.data.room.item

import androidx.room.Entity

@Entity(tableName = "items_table")
data class ItemsEntity(
    var id: String
) {
}