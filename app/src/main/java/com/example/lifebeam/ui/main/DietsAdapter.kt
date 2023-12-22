package com.example.lifebeam.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lifebeam.R
import com.example.lifebeam.data.remote.response.ActivitiesItem
import com.example.lifebeam.data.remote.response.DietsItem
import com.example.lifebeam.databinding.ItemRowActivitiesBinding
import com.example.lifebeam.databinding.ItemRowDietBinding

class DietsAdapter(private val activities: List<DietsItem>): RecyclerView.Adapter<DietsAdapter.DietViewHolder>(){
    class DietViewHolder(val binding: ItemRowDietBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DietsItem){
            binding.apply {
                tvDietName.text = data.food.name
                tvDietAmount.text = itemView.context.getString(R.string.diet_amount,data.amount.toString())
                tvDietCalories.text = itemView.context.getString(R.string.diet_calories, (data.food.calorieAmount * data.amount).toString())
                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, UpdateDailyActivity::class.java)
                    intent.putExtra("isActivty", false)
                    intent.putExtra("updateId", data.food.id)
                    intent.putExtra("updateName", data.food.name)
                    intent.putExtra("updateAmount", data.amount)
                    intent.putExtra("updateUnit", "")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val binding = ItemRowDietBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return DietViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size
}