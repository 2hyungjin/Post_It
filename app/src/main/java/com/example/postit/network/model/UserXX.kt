package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class UserXX(
    val genderId: Int,
    val name: String,
    val profile: String,
    @SerializedName("user_Id")
    val userId: Int
)