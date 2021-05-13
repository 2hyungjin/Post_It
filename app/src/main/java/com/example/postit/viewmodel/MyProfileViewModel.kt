package com.example.postit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.postit.network.model.UserXXX

class MyProfileViewModel : ViewModel() {
    var isMyProfile:Boolean=true
    lateinit var userXXX:UserXXX

}