package com.example.lifebeam.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {
    private var _dailyId:String = ""
    private var _token:String = ""

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.login(user)
        }
    }

    fun updateSession(user: UserModel) {
        viewModelScope.launch {
            repository.updateSession(user)
        }
    }

    fun logout(){
        viewModelScope.launch {
            repository.logOut()
        }
    }

}