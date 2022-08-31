package core

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

/**
 * An object containing useful constants, commonly used variables, and methods
 */
object Util {
	/**
	 * The current system time, with seconds and nanoseconds
	 */
	private val untrimmedNow: LocalTime = Clock.System.now()
		.toLocalDateTime(TimeZone.currentSystemDefault()).time
	
	/**
	 * The current system time.
	 */
	val NOW: LocalTime = LocalTime(untrimmedNow.hour, untrimmedNow.minute)
	
	/**
	 * Gets the current system time, minus the given number of minutes.
	 */
	fun nowMinus(minutes: Int): LocalTime {
		val untrimmedTime = Clock.System.now()
			.minus(minutes, DateTimeUnit.MINUTE)
			.toLocalDateTime(TimeZone.currentSystemDefault()).time
		return LocalTime(untrimmedTime.hour, untrimmedTime.minute)
	}
	
	/**
	 * The current date, according to the system.
	 */
	val TODAY: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
	
	/**
	 * The current date, minus the given number of days
	 */
	fun todayMinus(days: Int): LocalDate = TODAY.minus(days, DateTimeUnit.DAY)
}
