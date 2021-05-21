package com.example.postit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.postit.network.model.UserXXX

class MyProfileViewModel : ViewModel() {
    lateinit var userXXX: UserXXX
    var profile=MutableLiveData<String>()
}