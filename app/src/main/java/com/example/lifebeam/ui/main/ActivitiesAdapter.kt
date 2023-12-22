package com.example.lifebeam.ui.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lifebeam.R
import com.example.lifebeam.data.remote.response.ActivitiesItem
import com.example.lifebeam.data.remote.response.DailyResponse
import com.example.lifebeam.databinding.ItemRowActivitiesBinding
import java.time.LocalDate

class ActivitiesAdapter(private val activities: List<ActivitiesItem>): RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder>(){
    class ActivitiesViewHolder(val binding: ItemRowActivitiesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ActivitiesItem){
            binding.apply {
                tvActivityName.text = data.exercise.name
                tvActivityAmount.text = itemView.context.getString(R.string.activity_amount,data.amount.toString(), data.exercise.unit)
                tvActivityCalories.text = itemView.context.getString(R.string.diet_calories, (data.exercise.calorieBurned * data.amount).toString())
                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, UpdateDailyActivity::class.java)
                    intent.putExtra("isActivty", true)
                    intent.putExtra("updateId", data.exercise.id)
                    intent.putExtra("updateName", data.exercise.name)
                    intent.putExtra("updateAmount", data.amount)
                    intent.putExtra("updateUnit", data.exercise.unit)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
        val binding = ItemRowActivitiesBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ActivitiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size
}