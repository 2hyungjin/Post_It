package com.example.postit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postit.network.model.Comments
import com.example.postit.network.model.Req
import com.example.postit.network.model.Res
import com.example.postit.repository.AppRepo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BoardVM(val repo: AppRepo) : ViewModel() {
    val getBoardRes = MutableLiveData<Res.Board>()
    val postBoardRes = MutableLiveData<Res.Res>()
    val getCommentsRes = MutableLiveData<Comments>()
    val postCommentsRes = MutableLiveData<Res.Res>()

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
                    Log.d("post123", "요청 보냄" + res.toString())
                } else {
                    Log.d("post123", res.toString())
                }
            }
        }
    }
    fun getComments(boardId: Int){
        viewModelScope.launch {
            repo.getComments(boardId).let { res->
                if (res.isSuccessful){
                    getCommentsRes.postValue(res.body())
                }else{
                    getCommentsRes.postValue(null)
                    Log.d("comments",res.message())
                    Log.d("comments", res.errorBody().toString())
                }
            }
        }
    }
    fun postComments(body:Req.ReqComments){
        viewModelScope.launch {
            repo.postComments(body).let {res->
                if (res.isSuccessful){
                    postCommentsRes.postValue(res.body())
                }
                else{
                    postCommentsRes.postValue(null)
                    Log.d("comments",res.message())
                    Log.d("comments",res.code().toString())
                    Log.d("comments",res.body().toString())
                    Log.d("comments",res.errorBody().toString())
                }
            }
        }
    }
}