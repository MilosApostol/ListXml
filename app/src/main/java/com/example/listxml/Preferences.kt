package com.example.listxml

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Preferences private constructor() {

    companion object {
        private val sharePref = Preferences()
        private lateinit var sharedPreferences: SharedPreferences

        fun getInstance(context: Context): Preferences {
            if (!Companion::sharedPreferences.isInitialized) {
                synchronized(Preferences::class.java) {
                    if (!Companion::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences(
                            context.packageName,
                            Activity.MODE_PRIVATE
                        )
                    }
                }
            }
            return sharePref
        }
    }



    fun putLong(key: String, value: Long) {
        sharedPreferences.edit()
            .putLong(key, value)
            .apply()
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }


}