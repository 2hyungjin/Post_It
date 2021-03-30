package com.example.postit.repository

import com.example.postit.network.Object.Companion.myRetrofit
import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.util.ArrayList

class AppRepo {
    //login
    suspend fun signIn(body: Req.ReqSignIn) = myRetrofit.signIn(body)
    suspend fun signUp(body: Req.ReqSignUp) = myRetrofit.signUp(body)
    suspend fun chkId(id: String) = myRetrofit.chkId(id)
    suspend fun autoLogin() = myRetrofit.autoLogin()

    //main
    suspend fun post(
        date: RequestBody,
        content: RequestBody,
        profile: Int,
        files: ArrayList<MultipartBody.Part>,
        show: RequestBody
    ) = myRetrofit.postContent(date,content,profile,files, show)
    suspend fun getBoard(boardIds:List<Int>)= myRetrofit.getBoards(boardIds)

}