package com.example.listxml.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.listxml.R
import com.example.listxml.databinding.ActivityUserBinding

class UserActivity : BaseActivity<ActivityUserBinding>() {
    override fun getViewBinding() = ActivityUserBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }
}