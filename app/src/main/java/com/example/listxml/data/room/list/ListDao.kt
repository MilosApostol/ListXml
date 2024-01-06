package com.example.listxml.data.room.list

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ListDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(listEntity: ListEntity)


    @Update
    abstract fun updateList(listEntity: ListEntity)


    @Delete
    abstract fun deleteList(listEntity: ListEntity)


    @Query("SELECT * FROM `list_table`")
    abstract fun getAll(): LiveData<List<ListEntity>>

    @Query("SELECT * FROM `list_table` WHERE id = :id")
    abstract fun getListById(id: String): ListEntity

    @Query("SELECT * FROM `list_table` WHERE userId = :userId")
    abstract fun getListsByUserId(userId: String): List<ListEntity>

    @Query("DELETE FROM `list_table`")
    abstract fun deleteAll()

}