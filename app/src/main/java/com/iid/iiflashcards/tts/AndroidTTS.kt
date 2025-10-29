package com.iid.iiflashcards.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import java.util.Locale
import javax.inject.Inject

/**
 * A helper class to encapsulate TextToSpeech logic.
 *
 * This class handles initialization, state, language setting,
 * and resource cleanup.
 *
 * @param context The application context.
 * @param initializationListener A listener to report initialization status.
 */
class AndroidTTS @Inject constructor(
    @param:ApplicationContext val context: Context,
) : TextToSpeech.OnInitListener, TTSHelper {

    /**
     * Listener interface to report initialization status.
     */
    interface TtsListener {
        /**
         * Called when TTS is successfully initialized.
         */
        fun onTtsInitSuccess()

        /**
         * Called when TTS initialization fails.
         */
        fun onTtsInitError()
    }

    private var initializationListener: TtsListener? = null

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val localeNL = Locale.Builder().setLanguage("nl").setRegion("NL").build()

    /**
     * Callback for TextToSpeech initialization.
     * This is called by the TextToSpeech engine when it's ready.
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language
            // Using Locale.getDefault() uses the user's device language
            val result = tts?.setLanguage(localeNL)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TtsHelper", "The specified language is not supported!")
                // Fallback to US English if default is not available
                val usResult = tts?.setLanguage(localeNL)
                if (usResult == TextToSpeech.LANG_MISSING_DATA || usResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TtsHelper", "US English is also not supported!")
                    initializationListener?.onTtsInitError()
                } else {
                    isInitialized = true
                    initializationListener?.onTtsInitSuccess()
                }
            } else {
                // Language successfully set
                isInitialized = true
                initializationListener?.onTtsInitSuccess()
                Log.i("TtsHelper", "TTS Initialized successfully.")
            }
        } else {
            // Initialization failed
            Log.e("TtsHelper", "TTS Initialization Failed! Status: $status")
            isInitialized = false
            initializationListener?.onTtsInitError()
        }
    }

    /**
     * Speaks the given text.
     *
     * @param text The text to be spoken.
     * current speech or TextToSpeech.QUEUE_ADD to queue it.
     */
    override suspend fun speak(text: String) {
        // * @param queueMode Use TextToSpeech.QUEUE_FLUSH (default) to interrupt
        val queueMode: Int = TextToSpeech.QUEUE_FLUSH
        if (!isInitialized && tts == null) {
            tts = TextToSpeech(context, this)
            delay(1000)
        }
        setSpeechRate(0.75f)
        tts?.speak(text, queueMode, null, null)
    }

    /**
     * Sets the speech rate.
     *
     * @param rate Speech rate. 1.0 is the normal speed.
     * Lower values slow down the speech, higher values speed it up.
     */
    fun setSpeechRate(rate: Float) {
        if (isInitialized) {
            tts?.setSpeechRate(rate)
        }
    }

    /**
     * Sets the speech pitch.
     *
     * @param pitch Speech pitch. 1.0 is the normal pitch.
     * Lower values lower the pitch, higher values raise it.
     */
    @Suppress("unused")
    fun setPitch(pitch: Float) {
        if (isInitialized) {
            tts?.setPitch(pitch)
        }
    }

    /**
     * Shuts down the TextToSpeech engine.
     * Call this in your Activity's or Fragment's onDestroy() method.
     */
    fun shutdown() {
        if (isInitialized) {
            tts?.stop()
            tts?.shutdown()
        }
        isInitialized = false
    }

    override fun clear() {
        shutdown()
    }
}
