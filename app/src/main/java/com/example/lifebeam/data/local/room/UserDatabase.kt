package com.example.lifebeam.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifebeam.data.local.entitiy.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun daoUser(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): UserDatabase {
            if(INSTANCE == null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java, "UserFavorite")
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }
}