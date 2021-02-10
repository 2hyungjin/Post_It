package com.example.postit

import java.text.SimpleDateFormat
import java.util.*

class Time {
    val time=System.currentTimeMillis()
    val format=SimpleDateFormat("yyyy-MM-dd")
    val getDate=format.format(time)

}