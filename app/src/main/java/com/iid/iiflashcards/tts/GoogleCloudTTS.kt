package com.iid.iiflashcards.tts

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.AudioEncoding
import com.google.cloud.texttospeech.v1.SynthesisInput
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.TextToSpeechSettings
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import com.iid.iiflashcards.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class GoogleCloudTTS @Inject constructor(
    @param:ApplicationContext
    private val context: Context
) : TTSHelper {
    private var mediaPlayer: MediaPlayer? = null
    private var ttsClient: TextToSpeechClient? = null

    /**
     * Initializes the TTS client by explicitly loading credentials from a raw resource.
     * This should be called from a background thread.
     */
    private fun initializeClient() {
        if (ttsClient == null) {
            try {
                // 1. Load the credentials from the raw resource file
                val credentialsStream = context.resources.openRawResource(R.raw.credentials)

                // 2. Create GoogleCredentials from the stream
                val googleCredentials = GoogleCredentials.fromStream(credentialsStream)

                // 3. Create a TextToSpeechSettings object with these credentials
                val ttsSettings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(googleCredentials))
                    .build()

                // 4. Create the client using these settings
                ttsClient = TextToSpeechClient.create(ttsSettings)

            } catch (e: IOException) {
                // This will catch errors like file not found, or parsing issues.
                e.printStackTrace()
                // Handle the initialization error (e.g., log it, show a message to the user)
            }
        }
    }

    /**
     * Synthesizes the given text into speech and plays it.
     * This method is a suspend function and should be called from a coroutine.
     *
     * @param text The text to be spoken.

     */
    override suspend fun speak(
        text: String,
    ) {
        if (text.isBlank()) return

        // * @param languageCode The language code (e.g., "en-US", "nl-NL").
        // * @param voiceName The gender of the voice (e.g., "Wavenet-A", "Neural2-A", "Neural2-B").
        val languageCode = "nl-NL"
        val voiceName = "Wavenet-B"

        withContext(Dispatchers.IO) {
            initializeClient()
            if (ttsClient == null) {
                // Client failed to initialize, cannot proceed.
                return@withContext
            }

            // Set the text input to be synthesized
            val input = SynthesisInput.newBuilder().setText(text).build()

            // Build the voice request
            val voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setName("${languageCode}-$voiceName") // Example of a high-quality Neural2 voice
                .build()

            // Select the type of audio file to be returned
            val audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .setSpeakingRate(0.75)
                .build()

            // Perform the text-to-speech request
            val response: SynthesizeSpeechResponse = ttsClient!!.synthesizeSpeech(
                input,
                voice,
                audioConfig
            )

            // Get the audio contents from the response
            val audioContents = response.audioContent
            val audioFile = createTempAudioFile(audioContents.toByteArray())

            withContext(Dispatchers.Main) {
                playAudio(audioFile)
            }
        }
    }

    /**
     * Creates a temporary file from the audio byte array.
     */
    @Throws(IOException::class)
    private fun createTempAudioFile(audioData: ByteArray): File {
        val tempFile = File.createTempFile("tts_audio", ".mp3", context.cacheDir)
        tempFile.deleteOnExit()
        FileOutputStream(tempFile).use { out ->
            out.write(audioData)
        }
        return tempFile
    }

    /**
     * Plays the audio from the given file path.
     * This must be called on the Main thread.
     */
    private fun playAudio(file: File) {
        // Stop any currently playing audio
        stop()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                setDataSource(file.absolutePath)
                prepareAsync() // Prepare async to not block the UI thread
                setOnPreparedListener {
                    it.start()
                }
                setOnCompletionListener {
                    stop()
                    file.delete() // Clean up the temp file
                }
            } catch (e: IOException) {
                e.printStackTrace()
                stop()
                file.delete()
            }
        }
    }

    /**
     * Stops and releases the MediaPlayer instance.
     */
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    /**
     * Shuts down the TTS client. Call this when the helper is no longer needed.
     */
    fun shutdown() {
        stop()
        ttsClient?.shutdown()
        ttsClient = null
    }

    override fun clear() {
        stop()
        shutdown()
    }
}