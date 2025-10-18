package com.dj.insulink.feature.domain.mappers

import com.dj.insulink.feature.data.room.entity.FriendEntity
import com.dj.insulink.feature.domain.models.Friend

fun FriendEntity.toDomain(): Friend {
    return Friend(
        id = id,
        userId = userId,
        friendId = friendId,
        friendName = friendName,
        friendLastGlucoseReadingValue = friendLastGlucoseReadingValue,
        friendsLastGlucoseReadingTime = friendsLastGlucoseReadingTime
    )
}

fun Friend.toEntity(): FriendEntity {
    return FriendEntity(
        id = id,
        userId = userId,
        friendId = friendId,
        friendName = friendName,
        friendLastGlucoseReadingValue = friendLastGlucoseReadingValue,
        friendsLastGlucoseReadingTime = friendsLastGlucoseReadingTime
    )
}

fun List<FriendEntity>.toDomain(): List<Friend> {
    return map { it.toDomain() }
}

fun List<Friend>.toEntity(): List<FriendEntity> {
    return map { it.toEntity() }
}
