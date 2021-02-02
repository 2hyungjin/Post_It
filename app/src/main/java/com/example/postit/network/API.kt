package com.example.postit.network

import android.security.identity.AccessControlProfile
import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface API {
    @POST("signin")
    suspend fun signIn(
        @Body body: Req.ReqSignIn
    ): Response<Res.ResSignIn>

    @POST("signup")
    suspend fun signUp(
        @Body body: Req.ReqSignUp
    ): Response<Res.Res> //성공시 1 아닐시 0

    @GET("user/idCheck")
    suspend fun chkId(
        @Query("userId") userId: String
    ): Response<Res.Res> //성공시 1 아닐시 0

    @POST("autosignin")
    suspend fun autoLogin(): Response<Res.ResSignIn>

    @Multipart
    @POST("board")
    suspend fun postContent(
        @Part("date") date:String,
        @Part("contents") content: String,
        @Part("profile") profile: Int,
        @Part("files") files:MultipartBody.Part,
        @Part("show") show:String
        )
}