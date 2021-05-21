package com.example.postit.network.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserXXX(
    val genderId: Int,
    var name: String,
    val profile: String,
    @SerializedName("user_Id")
    val userId: Int
) : Serializable