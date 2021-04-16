package com.example.postit.network.model


import com.google.gson.annotations.SerializedName

object Res {
    data class ResSignIn(
        val result: Int,
        val token: String,
        val message: String
    )
    data class Res(
        val result: Int,
        val message: String
    )

    data class Board(
        val findBoard: List<FindBoard>
    )
    data class FindBoard(
        val boardId: Int,
        val contents: String,
        val date: Any,
        val images: Any,
        val like: Boolean,
        val likeNum: Int,
        val showId: String,
        val user: User,
        @SerializedName("user_Id")
        val userId: Int
    )
    data class User(
        val gender: Int,
        val profile: Int,
        val userName: String
    )
}

