package com.example.lifebeam.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lifebeam.data.remote.request.ActivitiesRequest
import com.example.lifebeam.data.remote.request.ActivitiesItem
import com.example.lifebeam.data.remote.request.DietsItem
import com.example.lifebeam.data.remote.request.DietsRequest
import com.example.lifebeam.data.remote.response.ActivitiesResponse
import com.example.lifebeam.data.remote.response.DailyResponse
import com.example.lifebeam.data.remote.response.DietsResponse
import com.example.lifebeam.data.remote.response.ExerciseResponse
import com.example.lifebeam.data.remote.response.FoodResponse
import com.example.lifebeam.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.HttpException

class DailyLogbookViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<DailyResponse>()
    val response: LiveData<DailyResponse> = _response

    private val _errResponse = MutableLiveData<String>()
    val errResponse: LiveData<String> = _errResponse

    private val _activitiesResponse = MutableLiveData<ActivitiesResponse>()
    val activitiesResponse: LiveData<ActivitiesResponse> = _activitiesResponse

    private val _dietResponse = MutableLiveData<DietsResponse>()
    val dietResponse: LiveData<DietsResponse> = _dietResponse

    private val _listExerciseResponse = MutableLiveData<ExerciseResponse>()
    val listExerciseResponse: LiveData<ExerciseResponse> = _listExerciseResponse

    private val _listFoodResponse = MutableLiveData<FoodResponse>()
    val listFoodResponse: LiveData<FoodResponse> = _listFoodResponse
    suspend fun getDailyLogbook(token: String = "", dailyId: String =""): DailyResponse {
        _isLoading.value = true
        try {
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.getDailyLogbook(dailyId)
            _isLoading.value = false
            _response.value = successResponse
            return successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DailyResponse::class.java)
            _isLoading.value = false
            _errResponse.value = errorResponse.message
            return  errorResponse
        }
    }

    suspend fun getExerciseList(token: String) {
        _isLoading.value = true
        try {
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.getExercise(1000, 0)
            _isLoading.value = false
            _listExerciseResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ExerciseResponse::class.java)
            _isLoading.value = false
            _listExerciseResponse.value = errorResponse
        }
    }
    suspend fun addActivity(token:String, activityList: List<ActivitiesItem>, dateTime: String){
        _isLoading.value = true
        try {
            val activity = ActivitiesRequest(dateTime,activityList)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.addActivity(activity)
            _isLoading.value = false
            _activitiesResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ActivitiesResponse::class.java)
            _isLoading.value = false
            _activitiesResponse.value = errorResponse
        }
    }

    suspend fun updateActivity(token:String, activityList: List<ActivitiesItem>, dailyId: String){
        _isLoading.value = true
        try {
            val activity = ActivitiesRequest("",activityList)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.updateActivity(dailyId, activity)
            _isLoading.value = false
            _activitiesResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ActivitiesResponse::class.java)
            _isLoading.value = false
            _activitiesResponse.value = errorResponse
        }
    }

    suspend fun getFoodList(token: String) {
        _isLoading.value = true
        try {
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.getFood(10000, 0)
            _isLoading.value = false
            _listFoodResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FoodResponse::class.java)
            _isLoading.value = false
            _listFoodResponse.value = errorResponse
        }
    }

    suspend fun addDiet(token:String, dietList: List<DietsItem>, dateTime: String){
        _isLoading.value = true
        try {
            val diet = DietsRequest(dateTime,dietList)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.addDiet(diet)
            _isLoading.value = false
            _dietResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DietsResponse::class.java)
            _isLoading.value = false
            _dietResponse.value = errorResponse
        }
    }

    suspend fun updateDiet(token:String, dietList: List<DietsItem>, dailyId: String){
        _isLoading.value = true
        try {
            val diet = DietsRequest("",dietList)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.updateDiet(dailyId, diet)
            _isLoading.value = false
            _dietResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DietsResponse::class.java)
            _isLoading.value = false
            _dietResponse.value = errorResponse
        }
    }
}