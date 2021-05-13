package com.example.postit.network.model


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

    data class User(
        val gender: Int,
        val profile: Int,
        val userName: String
    )
}

