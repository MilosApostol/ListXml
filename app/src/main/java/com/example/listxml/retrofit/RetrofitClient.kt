package com.example.listxml.retrofit

import com.example.listxml.Constants
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL_Items)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

object ApiItemsClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}