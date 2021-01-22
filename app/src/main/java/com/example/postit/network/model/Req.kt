package com.example.postit.network.model

import java.util.*

object Req {
    data class ReqSignIn(val userId: String, val password: String, val long: Int)
    data class ReqSignUp(
        val userId: String,
        val password: String,
        val name: String,
    )
}