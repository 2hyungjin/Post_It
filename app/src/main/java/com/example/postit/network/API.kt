package com.example.postit.network

import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("signin")
    suspend fun signIn(
        @Body body: Req.ReqSignIn
    ): Response<Res.ResSignIn>
    @POST("signup")
    suspend fun signUp(
        @Body body: Req.ReqSignUp
    ):Response<Int> //성공시 1 아닐시 0
    @POST("/user/idCheck")
    suspend fun chkid(
        @Body id:String
    ):Response<Int> //성공시 1 아닐시 0

}