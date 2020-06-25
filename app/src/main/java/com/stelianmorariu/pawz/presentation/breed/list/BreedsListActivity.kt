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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.databinding.ActivityBreedsListBinding
import com.stelianmorariu.pawz.domain.dagger.utils.Injectable
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.presentation.common.SimpleItemClickListener
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

    /**
     * Called when the user taps a breed.
     */
    override fun onItemClicked(item: DogBreed) {
        // todo: this will navigate to breed image gallery
        Snackbar.make(binding.root, "Tapped on ${item.displayName}", Snackbar.LENGTH_SHORT).show()
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
        binding.root.transitionToState(R.id.breed_list_state_expanded)
        breedsAdapter.setItems(viewState.breeds)
    }

    private fun renderLoadingState() {
        loading = true
        binding.root.transitionToState(R.id.breed_list_state_loading)
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
        binding.root.transitionToState(R.id.breed_list_state_error)

        Glide.with(this)
            .load(viewState.pawzError.imageId)
            .into(binding.errorLayout.imageView)

        binding.errorLayout.errorMessageTv.text =
            getString(viewState.pawzError.localizedDisplayMessageResId)
    }

    private fun initRecyclerView() {
        binding.breedsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = breedsAdapter
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, BreedsListActivity::class.java)
        }

    }
}