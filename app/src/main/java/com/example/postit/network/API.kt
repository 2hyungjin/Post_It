package com.example.postit.network

import com.example.postit.network.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface API {
    // Login
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

    // board
    @Multipart
    @POST("board")
    suspend fun postContent(
        @PartMap body: HashMap<String, RequestBody>,
        @Part("profile") profile: Int = 0,
        @Part files: List<MultipartBody.Part>?
    ): Response<Res.Res>

    @GET("board")
    suspend fun getBoards(
        @Query("boardIds") boardId: String
    ): Response<Res.Board>

    @POST("board/like/{boardId}")
    suspend fun like(
        @Path("boardId") boardId: Int
    ): Response<Res.Res>

    @HTTP(method = "DELETE", path = "board", hasBody = true)
    suspend fun deleteBoard(
        @Query("boardId") boardId: Int
    ): Response<Res.Res>

    @GET("comments")
    suspend fun getComments(
        @Query("boardId") boardId: Int,
        @Query("comments_Ids") comments_Ids: Int = -1
    ): Response<Comments>

    @POST("comments")
    suspend fun postComments(
        @Body body: Req.ReqComments
    ): Response<Res.Res>

    @GET("/user/{user_Id}")
    suspend fun getProfile(
        @Path("user_Id") user_Id: Int,
        @Query("boardIds") boardIds: String
    ): Response<Profile>

    @GET("yourProfile")
    suspend fun getMyProfile(): Response<MyProfile>

    @PUT("user/change/name")
    suspend fun changeUserName(@Query("name") name: String): Response<Res.Res>

    @PUT("user/change/password")
    suspend fun changeUserPassword(
        @Query("password") password: String,
        @Query("changePassword") changePassword: String
    ): Response<Res.Res>

    @Multipart
    @POST("board")
    suspend fun changeProfile(
        @PartMap body: HashMap<String, RequestBody>,
        @Part("profile") profile: Int = 1,
        @Part files: MultipartBody.Part
    ): Response<Res.Res>

}