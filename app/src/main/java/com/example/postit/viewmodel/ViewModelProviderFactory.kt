package com.example.postit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postit.repository.AppRepo

class ViewModelProviderFactory(
    private val repo:AppRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVM::class.java)){
            return LoginVM(repo) as T
        }
        else if (modelClass.isAssignableFrom(BoardVM::class.java)){
            return BoardVM(repo) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}