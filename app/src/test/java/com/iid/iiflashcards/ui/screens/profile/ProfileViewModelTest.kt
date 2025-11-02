package com.iid.iiflashcards.ui.screens.profile

import com.iid.iiflashcards.data.sharedpref.SettingsPreferences
import com.iid.iiflashcards.tts.TTSHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProfileViewModel
    private val settingsPreferences: SettingsPreferences = mock()
    private val ttsHelper: TTSHelper = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `initial state is correct when cloud tts is enabled`() = runTest {
        whenever(settingsPreferences.isCloudTSSAgentEnabled()).thenReturn(true)
        viewModel = ProfileViewModel(settingsPreferences, ttsHelper)

        assertTrue(viewModel.uiState.value.isCloudTTsEnabled)
    }

    @Test
    fun `initial state is correct when cloud tts is disabled`() = runTest {
        whenever(settingsPreferences.isCloudTSSAgentEnabled()).thenReturn(false)
        viewModel = ProfileViewModel(settingsPreferences, ttsHelper)

        assertFalse(viewModel.uiState.value.isCloudTTsEnabled)
    }

    @Test
    fun `toggles cloud tts from enabled to disabled`() = runTest {
        whenever(settingsPreferences.isCloudTSSAgentEnabled()).thenReturn(true)
        viewModel = ProfileViewModel(settingsPreferences, ttsHelper)

        viewModel.doAction(ProfileViewModel.Action.ToggleCloudTTS)

        verify(settingsPreferences).setTssAgent(false)
        verify(ttsHelper).clear()
        assertFalse(viewModel.uiState.value.isCloudTTsEnabled)
    }

    @Test
    fun `toggles cloud tts from disabled to enabled`() = runTest {
        whenever(settingsPreferences.isCloudTSSAgentEnabled()).thenReturn(false)
        viewModel = ProfileViewModel(settingsPreferences, ttsHelper)

        viewModel.doAction(ProfileViewModel.Action.ToggleCloudTTS)

        verify(settingsPreferences).setTssAgent(true)
        verify(ttsHelper).clear()
        assertTrue(viewModel.uiState.value.isCloudTTsEnabled)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}