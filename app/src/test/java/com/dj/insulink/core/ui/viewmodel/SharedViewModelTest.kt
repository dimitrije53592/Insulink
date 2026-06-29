package com.dj.insulink.core.ui.viewmodel

import android.content.Context
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: SharedViewModel

    @Before
    fun setUp() {
        authRepository = mockk()
        viewModel = SharedViewModel(authRepository)
    }

    @Test
    fun `isUserLoggedIn delegates to repository`() {
        every { authRepository.isUserLoggedIn() } returns true
        assertTrue(viewModel.isUserLoggedIn())

        every { authRepository.isUserLoggedIn() } returns false
        assertFalse(viewModel.isUserLoggedIn())
    }

    @Test
    fun `currentUser starts null`() {
        assertNull(viewModel.currentUser.value)
    }

    @Test
    fun `getCurrentUser loads the user into state`() = runTest(mainDispatcherRule.dispatcher) {
        val user = User(uid = "u1", firstName = "Jane", lastName = "Doe", email = "j@d.com", friendCode = "ABC123")
        coEvery { authRepository.getCurrentUser() } returns user

        viewModel.getCurrentUser()
        advanceUntilIdle()

        assertEquals(user, viewModel.currentUser.value)
    }

    @Test
    fun `signOut delegates to repository`() {
        val context = mockk<Context>()
        every { authRepository.signOut(context) } returns Unit

        viewModel.signOut(context)

        verify { authRepository.signOut(context) }
    }
}
