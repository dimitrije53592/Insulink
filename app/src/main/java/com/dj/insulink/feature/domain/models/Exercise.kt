package com.dj.insulink.feature.domain.models

data class Exercise(
   val id: Long,
   val userId: String,
   val sportName: String,
   val durationHours: Int,
   val durationMinutes: Int,
   val glucoseBefore: Int,
   val glucoseAfter: Int
)
