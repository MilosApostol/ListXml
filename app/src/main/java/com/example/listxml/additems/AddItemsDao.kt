package com.example.listxml.additems

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface AddItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(coinsList: List<AddItemsEntity>)

    @Query("SELECT * FROM AddItemsEntity ORDER BY price DESC")
    fun getAllCoins(): LiveData<List<AddItemsEntity>>
}