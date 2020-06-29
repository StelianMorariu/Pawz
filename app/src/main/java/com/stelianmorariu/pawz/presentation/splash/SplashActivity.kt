/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.stelianmorariu.pawz.databinding.ActivitySplashBinding
import com.stelianmorariu.pawz.presentation.breed.list.BreedsListActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // could pre-fetch data or check for updates
        Handler().postDelayed({ navigateToMainScreen() }, 1000)
    }

    private fun navigateToMainScreen() {
        startActivity(BreedsListActivity.newIntent(this@SplashActivity))
        finish()
    }
}

