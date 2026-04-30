package com.dj.insulink.feature.friends.domain.models

data class Friend(
    val id: Long,
    val userId: String,
    val friendId: String,
    val friendName: String,
    val friendLastGlucoseReadingValue: Int?,
    val friendsLastGlucoseReadingTime: Long?
)