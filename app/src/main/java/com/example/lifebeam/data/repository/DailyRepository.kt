package com.example.lifebeam.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.lifebeam.data.paging.ExercisePagingSource
import com.example.lifebeam.data.remote.response.ExercisesItem

class DailyRepository(private val repository: UserRepository) {

    private var token : LiveData<UserModel> = repository.getSession().asLiveData()
    fun getActivities(): LiveData<PagingData<ExercisesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ExercisePagingSource(token.value?.token ?: "")
            }
        ).liveData
    }
}