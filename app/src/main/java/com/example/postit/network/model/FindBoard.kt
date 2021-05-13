package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class FindBoard(
    val boardId: Int,
    val canDelete: Boolean,
    val contents: String,
    val date: String,
    val images: Any,
    var like: Boolean,
    var likeNum: Int,
    val showId: String,
    val user: UserXXXX,
    @SerializedName("user_Id")
    val userId: Int
)