package com.example.postit.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://noons.herokuapp.com/"

class Object {
    companion object{
        val retrofit: Retrofit by lazy {
         Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        }
        val myRetrofit: API by lazy { retrofit.create(API::class.java) }
    }
}