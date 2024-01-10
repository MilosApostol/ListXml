package com.example.listxml.additems

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listxml.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddItemsRep @Inject constructor(private val apiService: ApiService, val dao: AddItemsDao) {

    fun getAllItems(): LiveData<List<AddItemsEntity>> = dao.getAllCoins()

    /*
        suspend fun getItems(): List<AddItemsEntity> {
            return apiService.getItems()
        }
     */

    suspend fun getItem(title: String): AddItemsEntity {
        return apiService.getItem(title)
    }

    suspend fun getItems() = withContext(Dispatchers.IO) {
        val itemsList = apiService.getItems()
        dao.insertItems(itemsList)
    }

    suspend fun fetchFirstPageItems() = withContext(Dispatchers.IO) {
        runCatching {
            val items = apiService.getItems()

            if (items != null) {
                dao.insertItems(items)
            }
        }
    }

}