package com.example.lifebeam.data.repository

import com.example.lifebeam.data.utils.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userPreference: UserPreference,
) {

    suspend fun login(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun updateSession(user: UserModel) {
        userPreference.updateSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logOut() {
        userPreference.logout()
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference)
        }.also { instance = it }
    }
}

