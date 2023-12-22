package com.example.lifebeam.data.remote.request

import com.google.gson.annotations.SerializedName

data class UserRequest(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("age")
	val age: Int
)
