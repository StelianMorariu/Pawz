/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
        when (viewState) {
            is ErrorState -> renderErrorState(viewState)
            is EmptyState -> renderEmptyState()
            is LoadingState -> renderLoadingState()
            is DisplayBreedsState -> renderBreedsList(viewState)
        }
    }

    private fun renderBreedsList(viewState: DisplayBreedsState) {
        breedsAdapter.setItems(viewState.breeds)
    }

    private fun renderLoadingState() {

    }

    private fun renderEmptyState() {

    }

    private fun renderErrorState(viewState: ErrorState) {

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