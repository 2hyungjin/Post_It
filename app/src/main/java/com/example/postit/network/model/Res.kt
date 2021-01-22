package com.example.postit.network.model


import com.google.gson.annotations.SerializedName
object Res{
    data class ResSignIn(
        val result: Int,
        val token: String
    )
    data class ResChkId(
        val result: Int
    )
    data class ResSignUp(
        val userId:String,
        val password:String,
        val name:String,
        val genderId:Int
    )
}

