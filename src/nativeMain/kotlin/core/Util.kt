package core

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

object Util {
	val NOW = Clock.System.now()
		.toLocalDateTime(TimeZone.currentSystemDefault()).time
	val TODAY = Clock.System.todayIn(TimeZone.currentSystemDefault())
}
