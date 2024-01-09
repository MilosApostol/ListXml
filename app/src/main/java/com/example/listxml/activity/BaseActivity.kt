package com.example.listxml.activity

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding
import javax.inject.Inject

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    lateinit var binding: B

    abstract fun getViewBinding(): B

    private var connectivityManager: ConnectivityManager? = null
    var connectionLost = false

    private val networkCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    if (connectionLost) {
                        onNetworkAvailable(true)
                    }
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    onNetworkAvailable(false)
                    connectionLost = true
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initUi(savedInstanceState)
        setViewListeners()
        observeData()

    }

    open fun initUi(savedInstanceState: Bundle?) {}
    open fun setViewListeners() {}
    open fun observeData() {}

    open fun onNetworkAvailable(available: Boolean) {}

    override fun onResume() {
        super.onResume()

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request =
                NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
            connectivityManager?.registerNetworkCallback(request, networkCallback)
        }
    }

    override fun onPause() {
        super.onPause()

        connectivityManager?.unregisterNetworkCallback(networkCallback)
        connectivityManager = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connectivityManager != null) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            connectivityManager = null
        }
    }
}