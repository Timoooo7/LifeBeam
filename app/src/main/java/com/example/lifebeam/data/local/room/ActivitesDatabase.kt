package com.example.lifebeam.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifebeam.data.local.entitiy.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class ActivitiesDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ActivitiesDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ActivitiesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ActivitiesDatabase::class.java, "quote_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}