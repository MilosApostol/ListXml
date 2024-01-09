package com.example.listxml.additems

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID


data class AddItemsEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("price")
    var price: String = "",
    var listCreatorId: String = ""
)