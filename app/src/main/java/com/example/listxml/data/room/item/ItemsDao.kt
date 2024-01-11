package com.example.listxml.data.room.item

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.listxml.data.room.list.ListEntity

@Dao
abstract class ItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(itemsEntity: ItemsEntity)

    @Update
    abstract fun updateItem(itemsEntity: ItemsEntity)

    @Delete
    abstract fun deleteItem(itemsEntity: ItemsEntity)

    @Query("SELECT * FROM `items_table`")
    abstract fun getAll(): LiveData<List<ItemsEntity>>

    @Query("SELECT * FROM `items_table` WHERE id = :id")
    abstract fun getItemsById(id:String): ItemsEntity

    @Query("SELECT * FROM `items_table` WHERE listParent = :listId")
    abstract fun getItemsWithListId(listId:String): LiveData<List<ItemsEntity>>
}