package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class UserXX(
    val genderId: Int,
    val name: String,
    val profile: Int,
    @SerializedName("user_Id")
    val userId: Int
)