package com.iid.iiflashcards.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.iid.iiflashcards.data.sharedpref.SettingsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        settingsPreferences.isCloudTSSAgentEnabled().let {
            _uiState.value = _uiState.value.copy(isCloudTTsEnabled = it)
        }
    }

    fun doAction(action: Action) {
        when (action) {
            is Action.ToggleCloudTTS -> toggleCloudTTS()
        }
    }

    private fun toggleCloudTTS() {
        val isCloudEnabled = _uiState.value.isCloudTTsEnabled
        settingsPreferences.setTssAgent(!isCloudEnabled)
        _uiState.value = _uiState.value.copy(isCloudTTsEnabled = !isCloudEnabled)
    }
    data class UIState(
        val isCloudTTsEnabled: Boolean = false,
    )

    sealed class Action {
        object ToggleCloudTTS : Action()
    }
}