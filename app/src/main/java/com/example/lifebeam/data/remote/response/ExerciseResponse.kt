package com.example.lifebeam.data.remote.response

import com.google.gson.annotations.SerializedName

data class ExerciseResponse(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("exercises")
	val exercises: List<ExercisesItem>,

	@field:SerializedName("message")
	val message: String
)

data class ExercisesItem(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("calorieBurned")
	val calorieBurned: Any,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)

data class Pagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("maxPage")
	val maxPage: Int
)
