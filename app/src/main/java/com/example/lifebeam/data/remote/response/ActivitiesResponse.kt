package com.example.lifebeam.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActivitiesResponse(

	@field:SerializedName("message")
	val message: String
)
