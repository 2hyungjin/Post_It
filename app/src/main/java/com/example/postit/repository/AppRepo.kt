package com.example.postit.repository

import com.example.postit.network.Object.Companion.myRetrofit
import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import retrofit2.Retrofit

class AppRepo {
    suspend fun signIn(body:Req.ReqSignIn)=myRetrofit.signIn(body)
    suspend fun signUp(body:Req.ReqSignUp)= myRetrofit.signUp(body)
    suspend fun chkId(id:String)= myRetrofit.chkId(id)
    suspend fun autoLogin()= myRetrofit.autoLogin()
}