package com.example.lifebeam.data.remote.request

import com.google.gson.annotations.SerializedName

data class BiWeeklyRequest(

	@field:SerializedName("weekTime")
	val weekTime: String,

	@field:SerializedName("weight")
	val weight: Float,

	@field:SerializedName("height")
	val height: Float
)
