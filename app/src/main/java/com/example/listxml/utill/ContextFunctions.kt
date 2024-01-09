package com.example.listxml.utill

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
class ContextFunctions {
    fun Context.showToast(@StringRes stringRes: Int) {
        Toast.makeText(this, getString(stringRes), Toast.LENGTH_SHORT).show()
    }

    fun Context.hasInternetConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}
//adding pictures
    /*
    fun Context.loadTokenImage(imageUrl: String?, destination: ImageView) {
        Glide.with(this).load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_coin)
            .into(destination)
    }

     */