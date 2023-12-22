package com.example.lifebeam.data.remote.request

import com.google.gson.annotations.SerializedName

data class ActivitiesRequest(

	@field:SerializedName("dateTime")
	val dateTime: String,

	@field:SerializedName("activities")
	val activities: List<ActivitiesItem>
)

data class ActivitiesItem(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("exerciseId")
	val exerciseId: String
)
