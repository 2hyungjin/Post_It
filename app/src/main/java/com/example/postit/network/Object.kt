package com.example.postit.network

import android.util.Log
import com.example.postit.network.Pref.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://noons.herokuapp.com/"

class Object {
    companion object {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        val myRetrofit: API by lazy { retrofit.create(API::class.java) }
    }

    class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.d("login", App.prefs.token.toString())
            val req =
                chain.request().newBuilder().addHeader("Authorization", App.prefs.token?:"").build()
            return chain.proceed(req)

        }
    }
}