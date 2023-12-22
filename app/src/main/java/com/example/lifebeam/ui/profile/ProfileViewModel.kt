package com.example.lifebeam.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.lifebeam.data.local.entitiy.User
import com.example.lifebeam.data.remote.request.BiWeeklyRequest
import com.example.lifebeam.data.remote.request.UserRequest
import com.example.lifebeam.data.remote.response.BiWeeklyResponse
import com.example.lifebeam.data.remote.response.DailyResponse
import com.example.lifebeam.data.remote.response.ResultResponse
import com.example.lifebeam.data.remote.response.UserResponse
import com.example.lifebeam.data.remote.retrofit.ApiConfig
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.data.repository.UserRepository
import com.google.gson.Gson
import retrofit2.HttpException

class ProfileViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _addResponse = MutableLiveData<String>()
    val addRespopnse: LiveData<String> = _addResponse
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse
    private val _biWeeklyResponse = MutableLiveData<BiWeeklyResponse>()
    val biWeeklyResponse: LiveData<BiWeeklyResponse> = _biWeeklyResponse
    private val _resultResponse = MutableLiveData<ResultResponse>()
    val resultResponse: LiveData<ResultResponse> = _resultResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun addUser(token:String, age: Int, gender: String){
        _isLoading.value = true
        try {
            val user = UserRequest(gender,age)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.addUser(user)
            _isLoading.value = false
            _addResponse.value = successResponse.message
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)
            _isLoading.value = false
            _addResponse.value = errorResponse.message
        }
    }

    suspend fun updateUser(token:String, age: Int, gender: String, userId: String){
        _isLoading.value = true
        try {
            val user = UserRequest(gender,age)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.updateUser(userId, user)
            _isLoading.value = false
            _addResponse.value = successResponse.message
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)
            _isLoading.value = false
            _addResponse.value = errorResponse.message
        }
    }

    suspend fun getUser(token: String, userId: String) {
        _isLoading.value = true
        try {
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.getUser(userId)
            _isLoading.value = false
            _userResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)
            _isLoading.value = false
            _addResponse.value = errorResponse.message
        }
    }

    suspend fun addBiWeekly(token:String, weekTime:String, height:Float, weight:Float){
        _isLoading.value = true
        try {
            val biWeekly = BiWeeklyRequest(weekTime, weight, height)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.addBiWeekly(biWeekly)
            _isLoading.value = false
            _biWeeklyResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, BiWeeklyResponse::class.java)
            _isLoading.value = false
            _biWeeklyResponse.value = errorResponse
            Log.d("adderror", errorResponse.message)
        }
    }
    suspend fun updateBiWeekly(token:String, weekTime:String, height:Float, weight:Float, biWeeklyId: String){
        _isLoading.value = true
        try {
            val biWeekly = BiWeeklyRequest(weekTime, weight, height)
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.updateBiWeekly(biWeeklyId, biWeekly)
            _isLoading.value = false
            _biWeeklyResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, BiWeeklyResponse::class.java)
            _isLoading.value = false
            _biWeeklyResponse.value = errorResponse
            Log.d("updateerror", errorResponse.message)
        }
    }

    suspend fun getBiWeekly(token: String, biWeeklyId: String) {
        _isLoading.value = true
        try {
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.getBiWeekly(biWeeklyId)
            _isLoading.value = false
            _biWeeklyResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, BiWeeklyResponse::class.java)
            _isLoading.value = false
            Log.d("geterror", errorResponse.message)
        }
    }

    suspend fun getResult(token: String, userId: String) {
        _isLoading.value = true
        try {
            /* Send data to database */
            val client = ApiConfig.getApiService(token)
            /* Get Response */
            val successResponse = client.getResult(userId)
            _isLoading.value = false
            _resultResponse.value = successResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResultResponse::class.java)
            _isLoading.value = false
            _resultResponse.value = errorResponse
        }
    }
}