package com.example.lifebeam.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lifebeam.data.local.entitiy.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Delete
    fun delete(user: User)
}