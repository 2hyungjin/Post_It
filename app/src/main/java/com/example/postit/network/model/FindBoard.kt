package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class FindBoard(
    val boardId: Int,
    val canDelete: Boolean,
    val contents: String,
    val date: String,
    val like: Boolean,
    val likeNum: Int,
    val showId: String,
    val user: UserX,
    @SerializedName("user_Id")
    val userId: Int
)