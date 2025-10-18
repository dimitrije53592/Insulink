package com.dj.insulink.feature.domain.models

data class Friend(
    val id: Long,
    val userId: String,
    val friendId: String,
    val friendName: String,
    val friendLastGlucoseReadingValue: Int?,
    val friendsLastGlucoseReadingTime: Long?
)
