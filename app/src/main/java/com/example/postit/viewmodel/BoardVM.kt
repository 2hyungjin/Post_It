package com.example.postit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postit.network.model.*
import com.example.postit.network.repository.AppRepo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BoardVM(private val repo: AppRepo) : ViewModel() {
    val getBoardRes = MutableLiveData<Res.Board>()
    val postBoardRes = MutableLiveData<Res.Res>()
    val deleteBoardRes = MutableLiveData<Res.Res>()

    val getCommentsRes = MutableLiveData<Comments>()
    val postCommentsRes = MutableLiveData<Res.Res>()
    val getProfileRes = MutableLiveData<Profile>()
    val getMyProfileRes = MutableLiveData<MyProfile>()

    val changeUserNameRes = MutableLiveData<Res.Res>()
    val changeUserPasswordRes = MutableLiveData<Res.Res>()
    val changeUserProfileRes=MutableLiveData<Res.Res>()
    fun getBoard(id: String) {
        viewModelScope.launch {
            repo.getBoard(id).let { res ->
                if (res.isSuccessful) {
                    getBoardRes.postValue(res.body())
                } else {
                    Log.d("BoardVM", res.body().toString())
                    getBoardRes.postValue(null)
                }
            }
        }
    }

    fun postBoard(body: HashMap<String, RequestBody>, files: List<MultipartBody.Part>) {
        viewModelScope.launch {
            repo.post(body, files).let { res ->
                if (res.isSuccessful) {
                    Log.d("post123", res.message())
                    Log.d("post123", res.body().toString())
                    postBoardRes.postValue(res.body())
                } else {
                    postBoardRes.postValue(null)
                }
            }
        }
    }

    fun likeBoard(boardId: Int) {
        viewModelScope.launch {
            repo.likeBoard(boardId).let { res ->
                if (res.isSuccessful) {
                    Log.d("post123", "요청 보냄$res")
                } else {
                    Log.d("post123", res.toString())
                }
            }
        }
    }

    fun getComments(boardId: Int) {
        viewModelScope.launch {
            repo.getComments(boardId).let { res ->
                if (res.isSuccessful) {
                    getCommentsRes.postValue(res.body())
                } else {
                    getCommentsRes.postValue(null)
                    Log.d("comments", res.message())
                    Log.d("comments", res.errorBody().toString())
                }
            }
        }
    }

    fun postComments(body: Req.ReqComments) {
        viewModelScope.launch {
            repo.postComments(body).let { res ->
                if (res.isSuccessful) {
                    postCommentsRes.postValue(res.body())
                } else {
                    postCommentsRes.postValue(null)
                    Log.d("comments", res.message())
                    Log.d("comments", res.code().toString())
                    Log.d("comments", res.body().toString())
                    Log.d("comments", res.errorBody().toString())
                }
            }
        }
    }

    fun getProfile(userId: Int, boardIds: String) {
        viewModelScope.launch {
            repo.getProfile(userId, boardIds).let { res ->
                if (res.isSuccessful) {
                    Log.d("profile", res.body().toString())
                    getProfileRes.postValue(res.body())
                } else {
                    getProfileRes.postValue(null)
                    Log.d("profile", "message : " + res.message())
                    Log.d("profile", "body : " + res.body().toString())
                    Log.d("profile", "code : " + res.code().toString())
                    Log.d("profile", "error body : " + res.errorBody().toString())
                }
            }
        }
    }

    fun deleteBoard(boardId: Int) {
        viewModelScope.launch {
            repo.deleteBoard(boardId).let { res ->
                if (res.isSuccessful) {
                    deleteBoardRes.postValue(res.body())
                } else {
                    getProfileRes.postValue(null)
                    Log.d("board", res.message())
                    Log.d("board", res.code().toString())
                    Log.d("board", res.body().toString())
                    Log.d("board", res.errorBody().toString())
                }
            }
        }
    }

    fun getMyProfile() {
        viewModelScope.launch {
            repo.getMyProfile().let { res ->
                if (res.isSuccessful) {
                    getMyProfileRes.postValue(res.body())
                } else {
                    getProfileRes.postValue(null)
                    Log.d("board", res.message())
                    Log.d("board", res.code().toString())
                    Log.d("board", res.body().toString())
                    Log.d("board", res.errorBody().toString())

                }
            }
        }
    }

    fun changeUserName(name: String) {
        viewModelScope.launch {
            repo.changeUserName(name).let { res ->
                if (res.isSuccessful) {
                    changeUserNameRes.postValue(res.body())
                } else {
                    changeUserNameRes.postValue(null)
                    Log.d("changeUserName", res.message())
                    Log.d("changeUserName", res.code().toString())
                    Log.d("changeUserName", res.body().toString())
                    Log.d("changeUserName", res.errorBody().toString())
                }
            }
        }
    }

    fun changeUserPassword(password: String, changePassword: String) {
        viewModelScope.launch {
            repo.changeUserPassword(password, changePassword).let { res ->
                if (res.isSuccessful) {
                    changeUserPasswordRes.postValue(res.body())
                } else {
                    changeUserPasswordRes.postValue(res.body())
                    Log.d("changeUserPassword", res.message())
                    Log.d("changeUserPassword", res.code().toString())
                    Log.d("changeUserPassword", res.body().toString())
                    Log.d("changeUserPassword", res.errorBody().toString())
                }
            }
        }
    }
    fun changeUserProfile(body: HashMap<String, RequestBody>, files: MultipartBody.Part){
        viewModelScope.launch {
            Log.d("changeUserProfile","aa")
            repo.changeUserProfile(body,files).let {res->
                if (res.isSuccessful){
                    changeUserProfileRes.postValue(res.body())
                }else{
                    changeUserProfileRes.postValue(res.body())
                    Log.d("changeUserProfile", res.message())
                    Log.d("changeUserProfile", res.code().toString())
                    Log.d("changeUserProfile", res.body().toString())
                    Log.d("changeUserProfile", res.errorBody().toString())
                }
            }
        }
    }
}