/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stelianmorariu.pawz.databinding.ListItemBreedBinding
import com.stelianmorariu.pawz.domain.model.DogBreed

class BreedListAdapter : RecyclerView.Adapter<BreedListAdapter.BreedViewHolder>() {

    private val items: MutableList<DogBreed> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BreedViewHolder {
        // we have only 1 view type
        val itemBinding =
            ListItemBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    fun setItems(breeds: List<DogBreed>) {
        val calculateDiff = DiffUtil.calculateDiff(BreedDiffCallback(items, breeds))
        items.clear()
        items.addAll(breeds)

        calculateDiff.dispatchUpdatesTo(this)
    }


    class BreedViewHolder(private val itemBreedBinding: ListItemBreedBinding) :
        RecyclerView.ViewHolder(itemBreedBinding.root) {
        fun bind(item: DogBreed) {
            itemBreedBinding.breedNameTv.text = item.displayName
        }
    }

    private class BreedDiffCallback(
        private val oldList: List<DogBreed>,
        private val newList: List<DogBreed>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].displayName.hashCode() == newList[newItemPosition].displayName.hashCode()


        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }
}