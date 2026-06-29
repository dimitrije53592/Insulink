package com.dj.insulink.auth.ui.viewmodel

import android.content.Context
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.util.AndroidPatternsTestSupport
import com.dj.insulink.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = mockk()
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setUp() {
        AndroidPatternsTestSupport.installEmailPattern()
        every { context.getString(any()) } returns "error"
        every { context.getString(any(), any()) } returns "error with arg"
        viewModel = RegistrationViewModel(context, authRepository)
    }

    private fun fillValidForm() {
        viewModel.setFirstName("Jane")
        viewModel.setLastName("Doe")
        viewModel.setEmailAddress("jane@example.com")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("password123")
        viewModel.setTermsOfServiceAccepted(true)
    }

    @Test
    fun `setters update state`() {
        fillValidForm()
        assertTrue(viewModel.firstName.value == "Jane")
        assertTrue(viewModel.lastName.value == "Doe")
        assertTrue(viewModel.emailAddress.value == "jane@example.com")
        assertTrue(viewModel.password.value == "password123")
        assertTrue(viewModel.confirmPassword.value == "password123")
        assertTrue(viewModel.termsOfServiceAccepted.value)

        viewModel.setShowErrorMessage(true)
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `submit with empty names shows an error`() {
        viewModel.onCreateAccountSubmit()
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `submit with malformed email shows an error`() {
        viewModel.setFirstName("Jane")
        viewModel.setLastName("Doe")
        viewModel.setEmailAddress("nope")
        viewModel.onCreateAccountSubmit()
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `submit with short password shows an error`() {
        viewModel.setFirstName("Jane")
        viewModel.setLastName("Doe")
        viewModel.setEmailAddress("jane@example.com")
        viewModel.setPassword("short")
        viewModel.onCreateAccountSubmit()
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `submit with mismatched passwords shows an error`() {
        viewModel.setFirstName("Jane")
        viewModel.setLastName("Doe")
        viewModel.setEmailAddress("jane@example.com")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("different123")
        viewModel.onCreateAccountSubmit()
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `submit without accepting terms shows an error`() {
        viewModel.setFirstName("Jane")
        viewModel.setLastName("Doe")
        viewModel.setEmailAddress("jane@example.com")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("password123")
        viewModel.setTermsOfServiceAccepted(false)
        viewModel.onCreateAccountSubmit()
        assertTrue(viewModel.showErrorMessage.value)
    }

    @Test
    fun `submit with a valid form registers the user`() = runTest(mainDispatcherRule.dispatcher) {
        fillValidForm()
        coEvery { authRepository.registerUser(any()) } returns
            User(uid = "u1", firstName = "Jane", lastName = "Doe", email = "jane@example.com", friendCode = "ABC123")

        viewModel.onCreateAccountSubmit()
        advanceUntilIdle()

        coVerify { authRepository.registerUser(any()) }
        assertTrue(viewModel.registrationSuccess.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `submit surfaces an error when registration fails`() = runTest(mainDispatcherRule.dispatcher) {
        fillValidForm()
        coEvery { authRepository.registerUser(any()) } throws RuntimeException("The email address is already in use")

        viewModel.onCreateAccountSubmit()
        advanceUntilIdle()

        assertFalse(viewModel.registrationSuccess.value)
        assertTrue(viewModel.showErrorMessage.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `submit maps a generic error message`() = runTest(mainDispatcherRule.dispatcher) {
        fillValidForm()
        coEvery { authRepository.registerUser(any()) } throws RuntimeException("something else")

        viewModel.onCreateAccountSubmit()
        advanceUntilIdle()

        assertTrue(viewModel.showErrorMessage.value)
    }
}
