package com.example.lifebeam.data.remote.response

import com.google.gson.annotations.SerializedName

data class DailyResponse(

	@field:SerializedName("activities")
	val activities: List<ActivitiesItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("diets")
	val diets: List<DietsItem>
)

data class Exercise(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("calorieBurned")
	val calorieBurned: Float,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)

data class DietsItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("food")
	val food: Food
)

data class Food(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("calorieAmount")
	val calorieAmount: Float
)

data class ActivitiesItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("exercise")
	val exercise: Exercise
)
