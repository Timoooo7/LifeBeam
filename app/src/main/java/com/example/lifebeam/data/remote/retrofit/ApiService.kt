package com.example.lifebeam.data.remote.retrofit

import com.example.lifebeam.data.remote.request.ActivitiesRequest
import com.example.lifebeam.data.remote.request.BiWeeklyRequest
import com.example.lifebeam.data.remote.request.DietsRequest
import com.example.lifebeam.data.remote.request.UserRequest
import com.example.lifebeam.data.remote.response.ActivitiesResponse
import com.example.lifebeam.data.remote.response.BiWeeklyResponse
import com.example.lifebeam.data.remote.response.DailyResponse
import com.example.lifebeam.data.remote.response.DietsResponse
import com.example.lifebeam.data.remote.response.ExerciseResponse
import com.example.lifebeam.data.remote.response.FoodResponse
import com.example.lifebeam.data.remote.response.ResultResponse
import com.example.lifebeam.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /* Daily Logbook */
    @GET("logbook/daily/{daily_id}")
    suspend fun getDailyLogbook(
        @Path("daily_id") dailyId: String
    ): DailyResponse

    @POST("logbook/daily/activity")
    suspend fun addActivity(
        @Body user: ActivitiesRequest
    ): ActivitiesResponse

    @PUT("logbook/daily/activity/{daily_id}")
    suspend fun updateActivity(
        @Path("daily_id") dailyId: String,
        @Body user: ActivitiesRequest
    ): ActivitiesResponse

    @POST("logbook/daily/diet")
    suspend fun addDiet(
        @Body user: DietsRequest
    ): DietsResponse

    @PUT("logbook/daily/diet/{daily_id}")
    suspend fun updateDiet(
        @Path("daily_id") dailyId: String,
        @Body user: DietsRequest
    ): DietsResponse

    /* User */
    @POST("user")
    suspend fun addUser(
        @Body user: UserRequest
    ): UserResponse

    @PUT("user/{user_id}")
    suspend fun updateUser(
        @Path("user_id") userId: String,
        @Body user: UserRequest
    ): UserResponse

    @GET("user/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: String
    ): UserResponse

    /* Bi-Weekly */
    @POST("logbook/bi-weekly")
    suspend fun addBiWeekly(
        @Body biWeekly: BiWeeklyRequest
    ): BiWeeklyResponse

    @PUT("logbook/bi-weekly/{bi_weekly_id}")
    suspend fun updateBiWeekly(
        @Path("bi_weekly_id") biWeeklyId: String,
        @Body biWeekly: BiWeeklyRequest
    ): BiWeeklyResponse

    @GET("logbook/bi-weekly/{bi_weekly_id}")
    suspend fun getBiWeekly(
        @Path("bi_weekly_id") biWeeklyId: String
    ): BiWeeklyResponse

    /* Exercise */
    @GET("exercise")
    suspend fun getExercise(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): ExerciseResponse
    /* Food */
    @GET("food")
    suspend fun getFood(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ): FoodResponse

    /* Result */
    @GET("predict/{user_id}")
    suspend fun getResult(
        @Path("user_id") userId: String
    ): ResultResponse
}