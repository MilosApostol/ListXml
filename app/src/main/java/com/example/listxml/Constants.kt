package com.example.listxml

import androidx.compose.runtime.compositionLocalOf

class Constants {
    companion object {
        const val Items: String = "items"
        const val Lists: String = "lists"
        const val BASE_URL = "https://run.mocky.io/"
        const val API_ENDPOINT = "v3/6c347c00-6bbf-480b-b044-d5a2bb99293b/"
        const val BASE_URL_Items = "https://run.mocky.io/"
        const val API_ITEMS_ENDPOINT = "v3/505bd53a-5ed6-47ff-bcf0-94d9912b4e17"
        val LocalNetworkState = compositionLocalOf<Boolean> { error("No NetworkStateProvider") }

    }
}