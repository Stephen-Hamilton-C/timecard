package core

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

/**
 * An object containing useful constants, commonly used variables, and methods
 */
object Util {
	/**
	 * The current system time.
	 */
	val NOW = Clock.System.now()
		.toLocalDateTime(TimeZone.currentSystemDefault()).time
	
	/**
	 * The current date, according to the system.
	 */
	val TODAY = Clock.System.todayIn(TimeZone.currentSystemDefault())
}
