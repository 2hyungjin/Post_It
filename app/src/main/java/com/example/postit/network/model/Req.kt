package com.example.postit.network.model

object Req {
    data class ReqSignIn(val userId:String, val password:String, val long:Int)
}