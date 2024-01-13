package com.example.listxml.utill

import android.content.Context

class ContextProvider(val context: Context) {


   fun hasInternetConnection() = context.hasInternetConnection()
}