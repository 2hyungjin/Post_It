package com.example.postit.network

import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("signin")
    suspend fun signIn(
        @Body req: Req.ReqSignIn
    ): Response<Res.ResSignIn>
}