package com.example.lifebeam.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lifebeam.data.local.model.UserModel
import com.example.lifebeam.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            repository.logOut()
        }
    }
}