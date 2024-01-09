package com.example.listxml.retrofit

import com.example.listxml.Constants
import com.example.listxml.activity.AddItems
import com.example.listxml.additems.AddItemsEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(Constants.API_ITEMS_ENDPOINT)
    suspend fun getItems(): List<AddItemsEntity>

    @GET("${Constants.API_ITEMS_ENDPOINT}/{title}")
    suspend fun getItem(@Path("title") title: String): AddItemsEntity
}