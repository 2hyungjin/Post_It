package com.example.postit.network

import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import retrofit2.Response
import retrofit2.http.*

interface API {
    @POST("signin")
    suspend fun signIn(
        @Body body: Req.ReqSignIn
    ): Response<Res.ResSignIn>

    @POST("signup")
    suspend fun signUp(
        @Body body: Req.ReqSignUp
    ):Response<Res.Res> //성공시 1 아닐시 0

    @GET("user/idCheck")
    suspend fun chkId(
        @Query("userId") userId:String
    ):Response<Res.Res> //성공시 1 아닐시 0

}