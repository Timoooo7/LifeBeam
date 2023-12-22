package com.example.lifebeam.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.login(user)
        }
    }

}