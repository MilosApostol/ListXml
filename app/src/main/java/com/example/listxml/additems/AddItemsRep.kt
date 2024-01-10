package com.example.listxml.additems

import com.example.listxml.retrofit.ApiService
import javax.inject.Inject

class AddItemsRep @Inject constructor(private val apiService: ApiService, val dao: AddItemsDao) {

    fun getAllItems() = dao.getAllCoins()

    suspend fun getItems(): List<AddItemsEntity> {
        return apiService.getItems()
    }

    suspend fun getItem(title: String): AddItemsEntity {
        return apiService.getItem(title)
    }
}