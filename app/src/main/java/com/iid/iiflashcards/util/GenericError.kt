package com.iid.iiflashcards.util

import android.util.Log

fun logGenericError(message: String? = null, e: Exception? = null) {
    val msg = message ?: "Something went wrong"
    (e ?: Exception(message)).printStackTrace()
    Log.w("TODO imn", msg)
}

fun logDebugMessage(message: String) = println("TODO imn: $message")
