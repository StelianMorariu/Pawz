/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.databinding.ActivityBreedGalleryBinding
import com.stelianmorariu.pawz.domain.dagger.utils.Injectable
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.presentation.common.loadImage
import com.stelianmorariu.pawz.presentation.common.widgets.GridLayoutItemSpaceDecorator
import javax.inject.Inject


class BreedGalleryActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityBreedGalleryBinding
    private lateinit var currentBreed: DogBreed

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BreedGalleryViewModel::class.java)
    }

    private var loadingAnimation: AnimatedVectorDrawable? = null
    private var loading = false

    private var breedGalleryAdaper = BreedGalleryAdapter()

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

        viewModel.viewState.observe(this, Observer { viewState ->
            updateUiState(viewState)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadDataIfNecessary(currentBreed)
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

    private fun updateUiState(viewState: BreedGalleryViewState) {
        stopAnimation()
        when (viewState) {
            is ErrorState -> renderErrorState(viewState)
            is LoadingState -> renderLoadingState()
            is DisplayGalleryState -> renderBreedImageGallery(viewState)
        }
    }

    private fun renderBreedImageGallery(viewState: DisplayGalleryState) {
        binding.root.transitionToState(R.id.breed_gallery_state_default)
        breedGalleryAdaper.setItems(viewState.images)
    }

    private fun renderLoadingState() {
        loading = true
        binding.root.transitionToState(R.id.breed_list_state_loading)
        loadingAnimation = binding.loadingLayout.loadingIv.drawable as AnimatedVectorDrawable
        startAnimating()
    }

    private fun renderErrorState(viewState: ErrorState) {
        binding.root.transitionToState(R.id.breed_list_state_error)

        binding.errorLayout.imageView.loadImage(viewState.pawzError.imageId)
        binding.errorLayout.errorMessageTv.text =
            getString(viewState.pawzError.localizedDisplayMessageResId)
    }

    private fun startAnimating() {
        loadingAnimation?.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                if (loading) {
                    (drawable as Animatable).start()
                }
            }
        })

        loadingAnimation?.start()
    }

    private fun stopAnimation() {
        loadingAnimation?.stop()
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
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_layout_item_spacing)
        val columns: Int = resources.getInteger(R.integer.gallery_columns)

        binding.breedImageRecyclerView.apply {
            adapter = breedGalleryAdaper
            layoutManager =
                GridLayoutManager(context, columns, RecyclerView.VERTICAL, false)
            overScrollMode = View.OVER_SCROLL_NEVER
            addItemDecoration(GridLayoutItemSpaceDecorator(spacingInPixels, 2))
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