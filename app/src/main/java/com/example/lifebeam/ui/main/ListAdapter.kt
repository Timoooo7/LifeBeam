package com.example.lifebeam.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lifebeam.data.remote.response.ExercisesItem
import com.example.lifebeam.databinding.ItemListBinding

class ListAdapter :
    PagingDataAdapter<ExercisesItem, ListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ExercisesItem) {
            binding.tvItem.text = data.name
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExercisesItem>() {
            override fun areItemsTheSame(oldItem: ExercisesItem, newItem: ExercisesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ExercisesItem, newItem: ExercisesItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}