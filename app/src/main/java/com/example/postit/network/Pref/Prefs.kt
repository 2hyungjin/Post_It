package com.example.postit.network.Pref

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Prefs(context: Context) {
    private val prefNm="mPref"
    private val prefs=context.getSharedPreferences(prefNm,MODE_PRIVATE)

    var token:String?
        get() = prefs.getString("token",null)
        set(value){
            prefs.edit().putString("token",value).apply()
        }
    var isAutoLoginChked:Boolean
        get()=prefs.getBoolean("al",false)
        set(value) {prefs.edit().putBoolean("al",value).apply()}
}