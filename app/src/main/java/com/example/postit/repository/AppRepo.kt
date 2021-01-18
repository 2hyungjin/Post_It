package com.example.postit.repository

import com.example.postit.network.Object.Companion.myRetrofit
import com.example.postit.network.model.Req
import retrofit2.Retrofit

class AppRepo {
    suspend fun signIn(body:Req.ReqSignIn)=myRetrofit.signIn(body.userId,body.password,body.long)
}