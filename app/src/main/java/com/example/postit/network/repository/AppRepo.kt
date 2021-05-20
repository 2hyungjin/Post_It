package com.example.postit.network.repository

import com.example.postit.network.Object.Companion.myRetrofit
import com.example.postit.network.model.Req
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepo {
    //login
    suspend fun signIn(body: Req.ReqSignIn) = myRetrofit.signIn(body)
    suspend fun signUp(body: Req.ReqSignUp) = myRetrofit.signUp(body)
    suspend fun chkId(id: String) = myRetrofit.chkId(id)
    suspend fun autoLogin() = myRetrofit.autoLogin()

    //board
    suspend fun post(
        body: HashMap<String, RequestBody>,
        files: List<MultipartBody.Part>
    ) = myRetrofit.postContent(body = body, files = files)

    suspend fun getBoard(boardIds: String) = myRetrofit.getBoards(boardIds)
    suspend fun likeBoard(boardId: Int) = myRetrofit.like(boardId)
    suspend fun deleteBoard(boardId: Int) = myRetrofit.deleteBoard(boardId)

    suspend fun getComments(boardId: Int) = myRetrofit.getComments(boardId)
    suspend fun postComments(body: Req.ReqComments) = myRetrofit.postComments(body)
    suspend fun getProfile(userId: Int, boardIds: String) = myRetrofit.getProfile(userId, boardIds)
    suspend fun getMyProfile() = myRetrofit.getMyProfile()

    suspend fun changeUserName(name: String) = myRetrofit.changeUserName(name)
    suspend fun changeUserPassword(password: String, changePassword: String) =
        myRetrofit.changeUserPassword(
            password,
            changePassword
        )

    suspend fun changeUserProfile(
        body: HashMap<String, RequestBody>,
        file: MultipartBody.Part
    ) = myRetrofit.changeProfile(body, files = file)
}