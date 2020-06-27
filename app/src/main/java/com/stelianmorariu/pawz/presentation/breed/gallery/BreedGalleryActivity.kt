/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.stelianmorariu.pawz.databinding.ActivityBreedGalleryBinding
import com.stelianmorariu.pawz.domain.dagger.utils.Injectable
import com.stelianmorariu.pawz.domain.model.DogBreed
import javax.inject.Inject

class BreedGalleryActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityBreedGalleryBinding
    private lateinit var currentBreed: DogBreed

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BreedGalleryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            currentBreed = intent.getParcelableExtra(BREED)!!
        } catch (e: Exception) {
            // todo show error message to user
            finish()
        }

        binding = ActivityBreedGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(false)
            it.title = currentBreed.displayName.toUpperCase()
        }

    }

    private fun initRecyclerView() {
        binding.breedImageRecyclerView.apply {

            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    companion object {

        private const val BREED = "com.stelianmorariu.pawz.BREED"

        fun newIntent(context: Context, breed: DogBreed): Intent {
            val intent = Intent(context, BreedGalleryActivity::class.java)
            intent.putExtra(BREED, breed)
            return intent
        }
    }
}