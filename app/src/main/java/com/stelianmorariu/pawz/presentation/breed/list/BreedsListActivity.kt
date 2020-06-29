/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.databinding.ActivityBreedsListBinding
import com.stelianmorariu.pawz.domain.dagger.utils.Injectable
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.presentation.breed.gallery.BreedGalleryActivity
import com.stelianmorariu.pawz.presentation.common.SimpleItemClickListener
import com.stelianmorariu.pawz.presentation.common.loadImage
import com.stelianmorariu.pawz.presentation.common.widgets.StackUpListItemAnimator
import javax.inject.Inject


class BreedsListActivity : AppCompatActivity(), Injectable, SimpleItemClickListener<DogBreed> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityBreedsListBinding
    private var breedsAdapter = BreedListAdapter(this)

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BreedListViewModel::class.java)
    }

    private var loadingAnimation: AnimatedVectorDrawable? = null
    private var loading = false

    // limit the number of times we process breed clicks
    private var canProcessItemClicks = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBreedsListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initRecyclerView()

        viewModel.viewState.observe(this, Observer { viewState ->
            updateUiState(viewState)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadDataIfNecessary()
    }

    override fun onResume() {
        super.onResume()
        canProcessItemClicks = true
    }

    /**
     * Called when the user taps a breed.
     */
    override fun onItemClicked(item: DogBreed) {
        if (canProcessItemClicks) {
            canProcessItemClicks = false
            startActivity(BreedGalleryActivity.newIntent(this, item))
        }
    }

    private fun updateUiState(viewState: BreedListViewState) {
        stopAnimation()
        when (viewState) {
            is ErrorState -> renderErrorState(viewState)
            is LoadingState -> renderLoadingState()
            is DisplayBreedsState -> renderBreedsList(viewState)
        }
    }

    private fun renderBreedsList(viewState: DisplayBreedsState) {
        binding.errorLayout.root.visibility = INVISIBLE
        binding.loadingLayout.root.visibility = INVISIBLE

        binding.breedsRecyclerView.refreshDrawableState()

        // we need to explicitly set state in order to correctly setup the recycler transition
        binding.root.setState(R.id.cs_breeds_collapsible_start, -1, -1)
        binding.root.setTransition(R.id.breed_list_transition_recycler_collapse)

        Handler().postDelayed({ breedsAdapter.setItems(viewState.breeds) }, 100)
    }

    private fun renderLoadingState() {
        loading = true
        binding.errorLayout.root.visibility = INVISIBLE
        binding.loadingLayout.root.visibility = VISIBLE

        loadingAnimation = binding.loadingLayout.loadingIv.drawable as AnimatedVectorDrawable
        startAnimating()
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

    private fun renderErrorState(viewState: ErrorState) {
        binding.loadingLayout.root.visibility = INVISIBLE
        binding.errorLayout.root.visibility = VISIBLE

        binding.errorLayout.imageView.loadImage(viewState.pawzError.imageId)
        binding.errorLayout.errorMessageTv.text =
            getString(viewState.pawzError.localizedDisplayMessageResId)
    }

    private fun initRecyclerView() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        binding.breedsRecyclerView.apply {
            itemAnimator = StackUpListItemAnimator(context, metrics.heightPixels)
            layoutManager = LinearLayoutManager(context)
            adapter = breedsAdapter
            overScrollMode = OVER_SCROLL_NEVER
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, BreedsListActivity::class.java)
        }

    }
}