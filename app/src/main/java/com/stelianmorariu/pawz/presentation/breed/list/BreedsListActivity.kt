/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import android.content.Context
import android.content.Intent
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
import com.stelianmorariu.pawz.presentation.common.widgets.PawzLoadingView
import com.stelianmorariu.pawz.presentation.common.widgets.StackUpListItemAnimator
import javax.inject.Inject


class BreedsListActivity : AppCompatActivity(), Injectable, SimpleItemClickListener<DogBreed>,
    PawzLoadingView.PawzLoadingAnimationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityBreedsListBinding
    private var breedsAdapter = BreedListAdapter(this)

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BreedListViewModel::class.java)
    }

    // limit the number of times we process breed clicks
    private var canProcessItemClicks = true

    private var unprocessedViewState: BreedListViewState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBreedsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.loadingView.pawzLoadingAnimationListener = this

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

    override fun onPawzLoadingAnimationCompleted() {
        unprocessedViewState?.let { processUiUpdate(it) }
    }

    private fun updateUiState(viewState: BreedListViewState) {
        unprocessedViewState = null

        if (binding.loadingView.isLoading) {
            if (viewState !is LoadingState) {
                binding.loadingView.stopAnimation()
                unprocessedViewState = viewState
            }
        } else {
            processUiUpdate(viewState)
        }
    }

    private fun processUiUpdate(viewState: BreedListViewState) {
        when (viewState) {
            is ErrorState -> renderErrorState(viewState)
            is LoadingState -> renderLoadingState()
            is DisplayBreedsState -> renderBreedsList(viewState)
        }
    }

    private fun renderBreedsList(viewState: DisplayBreedsState) {
        binding.errorLayout.root.visibility = INVISIBLE
        binding.loadingView.visibility = INVISIBLE

        binding.breedsRecyclerView.refreshDrawableState()

        // we need to explicitly set state in order to correctly setup the recycler transition
        binding.root.setState(R.id.cs_breeds_collapsible_start, -1, -1)
        binding.root.setTransition(R.id.breed_list_transition_recycler_collapse)

        Handler().postDelayed({ breedsAdapter.setItems(viewState.breeds) }, 100)
    }

    private fun renderLoadingState() {
        binding.errorLayout.root.visibility = INVISIBLE
        binding.loadingView.visibility = VISIBLE
        binding.loadingView.startAnimating()
    }

    private fun renderErrorState(viewState: ErrorState) {
        binding.loadingView.visibility = INVISIBLE
        binding.errorLayout.root.visibility = VISIBLE

        binding.errorLayout.imageView.loadImage(viewState.pawzError.imageId)
        if (viewState.pawzError.message.isEmpty()) {
            binding.errorLayout.errorMessageTv.text =
                getString(viewState.pawzError.localizedDisplayMessageResId)
        } else {
            binding.errorLayout.errorMessageTv.text = viewState.pawzError.message
        }
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