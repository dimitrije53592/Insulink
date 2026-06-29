package com.dj.insulink.auth.ui.viewmodel

import android.content.Context
import android.widget.Toast
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.util.AndroidPatternsTestSupport
import com.dj.insulink.util.MainDispatcherRule
import com.google.firebase.auth.AuthCredential
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = mockk()
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        AndroidPatternsTestSupport.installEmailPattern()
        mockkStatic(Toast::class)
        every { Toast.makeText(any(), any<CharSequence>(), any()) } returns mockk(relaxed = true)
        every { context.getString(any()) } returns "error"
        every { context.getString(any(), any()) } returns "error with arg"
        viewModel = LoginViewModel(context, authRepository)
    }

    @After
    fun tearDown() {
        unmockkStatic(Toast::class)
    }

    @Test
    fun `setters update email and password state`() {
        viewModel.setEmail("a@b.com")
        viewModel.setPassword("secret")
        assertEquals("a@b.com", viewModel.email.value)
        assertEquals("secret", viewModel.password.value)

        viewModel.onEmailChange("c@d.com")
        viewModel.onPasswordChange("other")
        assertEquals("c@d.com", viewModel.email.value)
        assertEquals("other", viewModel.password.value)
    }

    @Test
    fun `setShowErrorMessage updates state`() {
        viewModel.setShowErrorMessage(true)
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `loginUser with empty email shows an error`() {
        viewModel.setEmail("")
        viewModel.setPassword("password123")

        viewModel.loginUser()

        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `loginUser with malformed email shows an error`() {
        viewModel.setEmail("not-an-email")
        viewModel.setPassword("password123")

        viewModel.loginUser()

        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `loginUser with valid email but short password shows an error`() {
        viewModel.setEmail("user@example.com")
        viewModel.setPassword("short")

        viewModel.loginUser()

        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `loginUser with valid credentials logs in successfully`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.setEmail("user@example.com")
        viewModel.setPassword("password123")
        coEvery { authRepository.loginUser(any()) } returns Unit

        viewModel.loginUser()
        advanceUntilIdle()

        coVerify { authRepository.loginUser(any()) }
        assertTrue(viewModel.loginSuccess.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loginUser surfaces an error when the repository throws`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.setEmail("user@example.com")
        viewModel.setPassword("password123")
        coEvery { authRepository.loginUser(any()) } throws RuntimeException("bad creds")

        viewModel.loginUser()
        advanceUntilIdle()

        assertFalse(viewModel.loginSuccess.value)
        assertTrue(viewModel.showErrorMessage.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `sendPasswordReset with blank email sets an error in the reset state`() {
        viewModel.setEmail("   ")

        viewModel.sendPasswordReset()

        assertNotNull(viewModel.passwordResetState.value.errorMessage)
    }

    @Test
    fun `sendPasswordReset success sets a success message`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.setEmail("user@example.com")
        coEvery { authRepository.sendPasswordResetEmail("user@example.com") } returns Unit

        viewModel.sendPasswordReset()
        advanceUntilIdle()

        assertNotNull(viewModel.passwordResetState.value.successMessage)
        assertFalse(viewModel.passwordResetState.value.isLoading)
    }

    @Test
    fun `sendPasswordReset failure sets an error message`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.setEmail("user@example.com")
        coEvery { authRepository.sendPasswordResetEmail(any()) } throws RuntimeException("no user")

        viewModel.sendPasswordReset()
        advanceUntilIdle()

        assertEquals("no user", viewModel.passwordResetState.value.errorMessage)
    }

    @Test
    fun `signInWithGoogle success marks login successful`() = runTest(mainDispatcherRule.dispatcher) {
        val credential: AuthCredential = mockk()
        coEvery { authRepository.signInWithGoogle(credential) } returns mockk()

        viewModel.signInWithGoogle(credential)
        advanceUntilIdle()

        assertTrue(viewModel.loginSuccess.value)
    }

    @Test
    fun `signInWithGoogle failure shows an error`() = runTest(mainDispatcherRule.dispatcher) {
        val credential: AuthCredential = mockk()
        coEvery { authRepository.signInWithGoogle(credential) } throws RuntimeException("denied")

        viewModel.signInWithGoogle(credential)
        advanceUntilIdle()

        assertFalse(viewModel.loginSuccess.value)
        assertTrue(viewModel.showErrorMessage.value)
    }
}
