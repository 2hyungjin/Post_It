package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class Profile(
    val findBoard: List<FindBoard>,
    val myProfile: Boolean,
    val result: Int,
    val user: UserXX
)