package com.dj.insulink.feature.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: String,
    val friendId: String,
    val friendName: String,
    val friendLastGlucoseReadingValue: Int?,
    val friendsLastGlucoseReadingTime: Long?
)
