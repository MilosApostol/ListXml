package com.example.listxml.activity

import android.content.Intent
import android.icu.util.Currency
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.listxml.R
import com.example.listxml.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
/*
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var billingManager: BillingManager
    private lateinit var consentInformation: ConsentInformation
    private lateinit var consentForm: ConsentForm

    private var darkModeSwitch: SwitchMaterial? = null
    private var defaultCurrencyFlag: TextView? = null
    private var doubleBackToExitPressedOnce = false
    private var editModeMenuItem: MenuItem? = null
    private var toolbarMenuId: Int = -1

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initUi(savedInstanceState: Bundle?) {
        if (isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setSupportActionBar(binding.converterToolbar)

        if (!hasInternetConnection()) {
            binding.noInternetText.isVisible = true
            connectionLost = true
        }
    }

    override fun observeData() {
        mainViewModel.apply {
            fetchStateChangedEvent.observe(this@MainActivity) { state ->
                when (state) {
                    FetchState.FETCHING_IN_PROGRESS -> {
                        binding.coinsFetchingText.isVisible = true
                    }

                    FetchState.FULL_FETCH_DONE -> {
                        binding.coinsFetchingText.isVisible = false
                    }

                    else -> {}
                }
            }
        }
    }


    override fun onNetworkAvailable(available: Boolean) {
        if (available) {
            mainViewModel.onNetworkRestored()
        }
        binding.noInternetText.isVisible = !available
    }
}

 */