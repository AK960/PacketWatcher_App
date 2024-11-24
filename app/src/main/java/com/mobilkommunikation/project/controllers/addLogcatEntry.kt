package com.mobilkommunikation.project.controllers

import android.util.Log

// Logging Infos:
//    Log.d("MyTag", "Message") --> Debug
//    Log.i("MyTag", "Message") --> Info
//    Log.w("MyTag", "Message") --> Warning
//    Log.e("MyTag", "Message") --> Error

fun myLog(
    type: String = "info",
    tag: String = "MyAppLog",
    msg: String
) {
    when (type) {
        "debug" -> Log.d(tag, msg)
        "info" -> Log.i(tag, msg)
        "warn" -> Log.w(tag, msg)
        "error" -> Log.e(tag, msg)
        else -> Log.v(tag, msg) // For non specified types
    }
}