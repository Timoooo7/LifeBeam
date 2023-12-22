package com.example.lifebeam.data.utils

import android.content.Context
import com.example.lifebeam.data.local.room.UserDatabase
import com.example.lifebeam.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
//        val database = UserDatabase.getInstance(context)
//        val dao = database.daoUser()
//        val appExecutors = AppExecutor()
        return UserRepository.getInstance(pref)
    }
}