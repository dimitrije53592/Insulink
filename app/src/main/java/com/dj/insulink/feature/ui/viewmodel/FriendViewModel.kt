package com.dj.insulink.feature.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.feature.data.repository.FriendRepository
import com.dj.insulink.feature.domain.models.Friend
import com.dj.insulink.feature.ui.screen.FriendsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Long
import kotlin.String

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    fun allFriendsForUser(userId: String) =
        friendRepository.getAllFriendsForUser(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _showAddNewFriendDialog = MutableStateFlow(false)
    val showAddNewFriendDialog = _showAddNewFriendDialog.asStateFlow()

    private val _enteredCode = MutableStateFlow("")
    val enteredCode = _enteredCode.asStateFlow()

    fun setShowAddNewFriendDialog(isVisible: Boolean) {
        _showAddNewFriendDialog.value = isVisible
    }

    fun setEnteredCode(code: String) {
        if (code.length <= 6) {
            _enteredCode.value = code.uppercase()
        }
    }

    fun onAddFriendClick(userId: String) {
        viewModelScope.launch {
            val foundUser = friendRepository.findUserByFriendCode(_enteredCode.value)
            foundUser?.let {
                friendRepository.addFriend(
                    Friend(
                        id = 0,
                        userId = userId,
                        friendId = foundUser.user.uid,
                        friendName = "${foundUser.user.firstName} ${foundUser.user.lastName}",
                        friendLastGlucoseReadingValue = foundUser.latestReading?.value,
                        friendsLastGlucoseReadingTime = foundUser.latestReading?.timestamp
                    )
                )

                friendRepository.pushFriendToFirestoreForUser(userId, foundUser.user.uid)
                friendRepository.pushFriendToFirestoreForUser(foundUser.user.uid, userId)
            }
        }
        setShowAddNewFriendDialog(false)
    }

    fun fetchFriendDataAndUpdateDatabase(userId: String) {
        viewModelScope.launch {
            friendRepository.fetchFriendDataAndUpdateDatabase(userId)
        }
    }

}