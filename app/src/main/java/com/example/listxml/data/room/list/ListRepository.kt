package com.example.listxml.data.room.list

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListRepository @Inject constructor(val dao: ListDao) {

    fun insertLists(listEntity: ListEntity){
        dao.insertList(listEntity)
    }
    fun getUsersListById(userId: String): List<ListEntity> {
        return dao.getListsByUserId(userId)
    }

    fun deleteList(listEntity: ListEntity){
        dao.deleteList(listEntity)
    }

    fun updateList(listEntity: ListEntity){
        dao.updateList(listEntity)
    }

    fun getListById(id: String): ListEntity {
        return dao.getListById(id)
    }
    /*
    fun getListsByUserId(userId: String): Flow<List<ListEntity>> {
        return dao.getListsByUserId(userId)
    }

     */

    fun getAllLists(): LiveData<List<ListEntity>>{
        return dao.getAll()
    }

    fun deleteAll(){
        return dao.deleteAll()
    }
}