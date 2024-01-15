package com.example.listxml.additems

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface AddItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(itemsList: List<AddItemsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: AddItemsEntity)
    @Query("SELECT * FROM AddItemsEntity ORDER BY price DESC")
    fun getAllItems(): LiveData<List<AddItemsEntity>>
}