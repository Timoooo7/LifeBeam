package com.example.lifebeam.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("age")
	val age: Int
)
