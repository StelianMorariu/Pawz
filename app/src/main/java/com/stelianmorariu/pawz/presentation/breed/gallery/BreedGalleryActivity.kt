/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.databinding.ActivityBreedGalleryBinding
import com.stelianmorariu.pawz.domain.dagger.utils.Injectable
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.presentation.common.SimpleItemClickListener
import com.stelianmorariu.pawz.presentation.common.loadImage
import com.stelianmorariu.pawz.presentation.common.loadImageNoCrop
import com.stelianmorariu.pawz.presentation.common.widgets.GalleryLinearSmoothScroller
import com.stelianmorariu.pawz.presentation.common.widgets.GridLayoutItemSpaceDecorator
import com.stelianmorariu.pawz.presentation.common.widgets.PawzLoadingView
import com.stfalcon.imageviewer.StfalconImageViewer
import javax.inject.Inject


class BreedGalleryActivity : AppCompatActivity(), Injectable,
    SimpleItemClickListener<Pair<Int, ImageView>>, PawzLoadingView.PawzLoadingAnimationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityBreedGalleryBinding
    private lateinit var currentBreed: DogBreed

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BreedGalleryViewModel::class.java)
    }

    private var breedGalleryAdaper = BreedGalleryAdapter(this)

    private var fullscreenImageViewwer: StfalconImageViewer<String>? = null

    private lateinit var smoothScroller: GalleryLinearSmoothScroller

    // limit the number of times we process breed clicks
    private var canProcessItemClicks = true

    private var unprocessedViewState: BreedGalleryViewState? = null

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

        smoothScroller = GalleryLinearSmoothScroller(this)

        initToolbar()
        initRecyclerView()

        binding.loadingView.pawzLoadingAnimationListener = this
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

    override fun onItemClicked(positionWithTargetPair: Pair<Int, ImageView>) {
        if (canProcessItemClicks) {
            canProcessItemClicks = false
            showFullScreenImageViewer(positionWithTargetPair)
        }
    }

    override fun onPawzLoadingAnimationCompleted() {
        unprocessedViewState?.let { processUiUpdate(it) }
    }

    private fun showFullScreenImageViewer(positionWithTargetPair: Pair<Int, ImageView>) {
        fullscreenImageViewwer = StfalconImageViewer.Builder<String>(
            this,
            breedGalleryAdaper.getItems(),
            ::loadFullScreenImage
        )
            .withStartPosition(positionWithTargetPair.first)
            .withTransitionFrom(positionWithTargetPair.second)
            .withImageChangeListener {
                updateImageViewerAnimationTarget(it)
            }
            .withHiddenStatusBar(false)
            .withDismissListener {
                canProcessItemClicks = true
            }
            .show()
    }

    private fun updateImageViewerAnimationTarget(targetPosition: Int) {
        // scroll the list to the target position to make sure we have a target image view
        smoothScroller.targetPosition = targetPosition
        val layoutManager = binding.breedImageRecyclerView.layoutManager as GridLayoutManager
        layoutManager.startSmoothScroll(smoothScroller)

        val targetIv: ImageView =
            binding.breedImageRecyclerView.layoutManager!!.findViewByPosition(targetPosition) as ImageView

        fullscreenImageViewwer?.updateTransitionImage(targetIv)
    }

    private fun loadFullScreenImage(imageView: ImageView, imageUrl: String) {
        imageView.apply {
            loadImageNoCrop(imageUrl, R.drawable.ic_placeholder)
        }
    }

    private fun updateUiState(viewState: BreedGalleryViewState) {
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

    private fun processUiUpdate(viewState: BreedGalleryViewState) {
        when (viewState) {
            is ErrorState -> renderErrorState(viewState)
            is LoadingState -> renderLoadingState()
            is DisplayGalleryState -> renderBreedImageGallery(viewState)
        }
    }

    private fun renderBreedImageGallery(viewState: DisplayGalleryState) {
        binding.errorLayout.root.visibility = View.INVISIBLE
        binding.loadingView.visibility = View.INVISIBLE
        binding.breedImageRecyclerView.visibility = View.VISIBLE

        breedGalleryAdaper.setItems(viewState.images)
    }

    private fun renderLoadingState() {
        binding.breedImageRecyclerView.visibility = View.INVISIBLE
        binding.errorLayout.root.visibility = View.INVISIBLE
        binding.loadingView.visibility = View.VISIBLE

        binding.loadingView.startAnimating()
    }

    private fun renderErrorState(viewState: ErrorState) {
        binding.breedImageRecyclerView.visibility = View.INVISIBLE
        binding.loadingView.visibility = View.INVISIBLE
        binding.errorLayout.root.visibility = View.VISIBLE

        binding.errorLayout.imageView.loadImage(viewState.pawzError.imageId)

        if (viewState.pawzError.message.isEmpty()) {
            binding.errorLayout.errorMessageTv.text =
                getString(viewState.pawzError.localizedDisplayMessageResId)
        } else {
            binding.errorLayout.errorMessageTv.text = viewState.pawzError.message
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