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


    suspend fun post(
        body:HashMap<String,RequestBody>,
        files:List<MultipartBody.Part>
    ) = myRetrofit.postContent(body = body,files = files)
    suspend fun getBoard(boardIds:String)= myRetrofit.getBoards(boardIds)
    suspend fun likeBoard(boardId: Int)= myRetrofit.like(boardId)
}