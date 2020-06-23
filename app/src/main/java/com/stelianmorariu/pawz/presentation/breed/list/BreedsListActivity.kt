/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stelianmorariu.pawz.databinding.ActivityBreedsListBinding
import com.stelianmorariu.pawz.domain.dagger.utils.Injectable
import javax.inject.Inject


class BreedsListActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityBreedsListBinding
    private var breedsAdapter = BreedListAdapter()

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

    private fun updateUiState(viewState: BreedListViewState) {
        TODO("Not yet implemented")
    }

    private fun initRecyclerView() {
        binding.breedsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = breedsAdapter
        }
    }
}