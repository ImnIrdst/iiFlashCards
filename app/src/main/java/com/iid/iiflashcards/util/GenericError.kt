package com.iid.iiflashcards.util

import android.content.Context
import android.util.Log
import android.widget.Toast

fun logGenericError(message: String? = null, e: Exception? = null) {
    val msg = message ?: "Something went wrong"
    (e ?: Exception(message)).printStackTrace()
    Log.w("TODO imn", msg)
}

@Suppress("unused")
fun debugLog(message: String) = println("TODO imn: $message")

fun Context.showNotImplementedToast(message: String = "Not implemented yet!") =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


