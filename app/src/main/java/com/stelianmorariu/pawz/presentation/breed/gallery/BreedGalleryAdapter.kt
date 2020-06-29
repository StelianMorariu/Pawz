/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.databinding.ListItemBreedImageBinding
import com.stelianmorariu.pawz.presentation.common.SimpleItemClickListener
import com.stelianmorariu.pawz.presentation.common.loadImage

class BreedGalleryAdapter(private val itemClickListener: SimpleItemClickListener<Pair<Int, ImageView>>) :
    RecyclerView.Adapter<BreedGalleryAdapter.BreedImageViewHolder>() {

    private val items: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BreedImageViewHolder {
        // we have only 1 view type
        val itemBinding =
            ListItemBreedImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedImageViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BreedImageViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    fun setItems(breeds: List<String>) {
        val calculateDiff = DiffUtil.calculateDiff(BreedImageItemDiffCallback(items, breeds))
        items.clear()
        items.addAll(breeds)

        calculateDiff.dispatchUpdatesTo(this)
    }

    fun getItems(): List<String> {
        return ArrayList(items)
    }


    class BreedImageViewHolder(private val itemBreedBinding: ListItemBreedImageBinding) :
        RecyclerView.ViewHolder(itemBreedBinding.root) {

        fun bind(
            item: String,
            itemClickListener: SimpleItemClickListener<Pair<Int, ImageView>>
        ) {
            itemBreedBinding.breedImageView.loadImage(item, R.drawable.ic_placeholder)
            itemBreedBinding.breedImageView.setOnClickListener { v ->
                v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                itemClickListener.onItemClicked(
                    Pair(
                        this.adapterPosition,
                        itemBreedBinding.breedImageView
                    )
                )
            }

        }
    }

    private class BreedImageItemDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()


        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }


}