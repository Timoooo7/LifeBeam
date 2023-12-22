package com.example.lifebeam.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lifebeam.data.repository.UserRepository
import com.example.lifebeam.data.utils.Injection
import com.example.lifebeam.ui.auth.LoginViewModel
import com.example.lifebeam.ui.main.MainActivity
import com.example.lifebeam.ui.main.MainViewModel
import com.example.lifebeam.ui.profile.ProfileViewModel

class ViewModelFactory(private val repository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java)->{
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideUserRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}