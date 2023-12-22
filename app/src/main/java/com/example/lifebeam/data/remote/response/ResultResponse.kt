package com.example.lifebeam.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("dayLeft")
	val dayLeft: Int,

	@field:SerializedName("dayLeftTo")
	val dayLeftTo: String,

	@field:SerializedName("calorieOffset")
	val calorieOffset: Float,

	@field:SerializedName("clientMessage")
	val clientMessage: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
