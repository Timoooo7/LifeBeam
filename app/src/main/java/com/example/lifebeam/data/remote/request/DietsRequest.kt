package com.example.lifebeam.data.remote.request

import com.google.gson.annotations.SerializedName

data class DietsRequest(

	@field:SerializedName("dateTime")
	val dateTime: String,

	@field:SerializedName("diets")
	val diets: List<DietsItem>
)

data class DietsItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("foodId")
	val foodId: String
)
