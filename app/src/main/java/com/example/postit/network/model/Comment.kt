package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

data class Comment(
    val boardId: Int,
    val childComments: Int,
    val comment: String,
    val commentsId: Int,
    @SerializedName("FcommentsId")
    val fcommentsId: Any,
    @SerializedName("file.filename")
    val fileFilename: Any,
    val like: Boolean,
    val likeNum: Int,
    val user: User,
    @SerializedName("user_Id")
    val userId: Int
)