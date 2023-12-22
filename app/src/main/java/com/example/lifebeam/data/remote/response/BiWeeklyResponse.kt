package com.example.lifebeam.data.remote.response

import com.google.gson.annotations.SerializedName

data class BiWeeklyResponse(

	@field:SerializedName("weight")
	val weight: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("height")
	val height: String
)
