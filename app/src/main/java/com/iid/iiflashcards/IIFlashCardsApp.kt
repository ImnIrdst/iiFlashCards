package com.iid.iiflashcards

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IIFlashCardsApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        setupTtsAuthentication()
//    }
//
//    private fun setupTtsAuthentication() {
//        // This is a simplified way to handle credentials for development.
//        // For production, consider more secure ways to store and access your key.
//        try {
//            resources.openRawResource(R.raw.credentials).use {
//                System.setProperty(
//                    "GOOGLE_APPLICATION_CREDENTIALS",
//                    GoogleCredentials.fromStream(it).toString()
//                )
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
}
