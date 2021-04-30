package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class UserX(
    val profile: Int,
    @SerializedName("user_id")
    val userId: Int,
    val userName: String
)