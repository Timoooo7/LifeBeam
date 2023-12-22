package com.example.lifebeam.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//@Entity(tableName = "food")
data class FoodResponse(
//	@PrimaryKey
	@field:SerializedName("pagination")
	val pagination: FoodPagination,

	@field:SerializedName("foods")
	val foods: List<FoodsItem>,

	@field:SerializedName("message")
	val message: String
)

data class FoodsItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("calorieAmount")
	val calorieAmount: Float
)

data class FoodPagination(

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("maxPage")
	val maxPage: Int
)
