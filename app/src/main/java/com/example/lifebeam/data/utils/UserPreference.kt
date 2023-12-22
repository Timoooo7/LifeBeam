package com.example.lifebeam.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lifebeam.data.repository.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    suspend fun saveSession(user: UserModel){
        dataStore.edit { preferences ->
            preferences[UID_KEY] = user.uid
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PHOTO_KEY] = user.photo
            preferences[PHONE_KEY] = user.phone
            preferences[TOKEN_KEY] = user.token
            preferences[DAILY_KEY] = user.daily_id
        }
    }

    suspend fun updateSession(user: UserModel){
        dataStore.edit { preferences ->
            preferences[UID_KEY] = user.uid
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PHOTO_KEY] = user.photo
            preferences[PHONE_KEY] = user.phone
            preferences[DAILY_KEY] = user.daily_id
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[UID_KEY]?: "",
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PHOTO_KEY]?: "",
                preferences[PHONE_KEY]?: "",
                preferences[TOKEN_KEY]?: "",
                preferences[DAILY_KEY]?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val UID_KEY = stringPreferencesKey("uid")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHOTO_KEY = stringPreferencesKey("photo")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val DAILY_KEY = stringPreferencesKey("daily_id")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}