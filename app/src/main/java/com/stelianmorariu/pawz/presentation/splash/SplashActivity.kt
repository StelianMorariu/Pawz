/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.presentation.breed.list.BreedsListActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        startActivity(BreedsListActivity.newIntent(this))
    }
}