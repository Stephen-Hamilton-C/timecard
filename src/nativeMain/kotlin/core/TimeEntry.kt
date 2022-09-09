package core

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class TimeEntry(
	val startTime: LocalTime,
	var endTime: LocalTime? = null,
) {}
