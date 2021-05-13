package com.example.postit.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.postit.R
import com.example.postit.network.model.UserXXX
import com.example.postit.viewmodel.MyProfileViewModel

class MyProfileActivity : AppCompatActivity() {
    lateinit var myProfileViewModel:MyProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        myProfileViewModel=ViewModelProvider(this)[MyProfileViewModel::class.java]
        val user=intent.getSerializableExtra("user") as UserXXX
        myProfileViewModel.userXXX=user
        Log.d("myProfile","activity : $user")

    }
}