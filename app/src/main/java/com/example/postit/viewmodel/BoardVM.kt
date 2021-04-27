package com.example.postit.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postit.network.model.Res
import com.example.postit.repository.AppRepo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BoardVM(val repo: AppRepo) : ViewModel() {
    val getBoardRes = MutableLiveData<Res.Board>()
    val postBoardRes = MutableLiveData<Res.Res>()
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

    }
}