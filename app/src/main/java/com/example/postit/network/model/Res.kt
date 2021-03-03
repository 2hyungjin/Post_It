package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

object Res {
    data class ResSignIn(
        val result: Int,
        val token: String,
        val message: String
    )
    data class Res(
        val result: Int
    )

}

