package com.example.lifebeam.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lifebeam.data.remote.response.ExerciseResponse
import com.example.lifebeam.data.remote.response.ExercisesItem
import com.example.lifebeam.data.remote.retrofit.ApiConfig
import com.example.lifebeam.data.remote.retrofit.ApiService

class ExercisePagingSource(token:String) : PagingSource<Int, ExercisesItem>() {
    private val _token = token
    override fun getRefreshKey(state: PagingState<Int, ExercisesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExercisesItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val responseData = ApiConfig.getApiService(_token).getExercise(10, position)
            LoadResult.Page(
                data = responseData.exercises,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData == null) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}