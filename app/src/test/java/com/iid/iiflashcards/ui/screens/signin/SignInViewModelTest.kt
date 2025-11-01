package com.iid.iiflashcards.ui.screens.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iid.iiflashcards.ui.helper.UserData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SignInViewModel


    private val testUserData = UserData("123", "Test User", null)

    @Before
    fun setUp() {
        viewModel = SignInViewModel()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.state.value
        assertFalse(state.isSignInSuccessful)
        assertNull(state.signInError)
        assertNull(state.userData)
    }

    @Test
    fun `set user updates state and is successful`() {
        viewModel.setUser(testUserData)

        val state = viewModel.state.value
        assertTrue(state.isSignInSuccessful)
        assertEquals(testUserData, state.userData)
    }

    @Test
    fun `set user with null clears user data and is not successful`() {
        // First set a user
        viewModel.setUser(testUserData)

        // Now set null user
        viewModel.setUser(null)

        val state = viewModel.state.value
        assertFalse(state.isSignInSuccessful)
        assertNull(state.userData)
    }

    @Test
    fun `get user returns correct user data`() {
        viewModel.setUser(testUserData)

        assertEquals(testUserData, viewModel.getUser())
    }

    @Test
    fun `signIn posts sign in event`() {
        viewModel.signIn()
        assertEquals(SignInViewModel.Event.SignIn, viewModel.event.value)
    }

    @Test
    fun `signOut posts sign out event`() {
        viewModel.signOut()
        assertEquals(SignInViewModel.Event.SignOut, viewModel.event.value)
    }

    @Test
    fun `reset state returns to initial state`() {
        viewModel.setUser(testUserData)

        viewModel.resetState()

        val state = viewModel.state.value
        assertFalse(state.isSignInSuccessful)
        assertNull(state.signInError)
        assertNull(state.userData)
    }
}