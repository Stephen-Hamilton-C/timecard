package core

import com.soywiz.korio.file.VfsFile
import config.Configuration
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
	private val config = Configuration.load()
	
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
	
	suspend fun mkdirRecursive(directory: VfsFile) {
		if(directory.exists()) return
		if(!directory.parent.exists()) mkdirRecursive(directory.parent)
		
		directory.mkdir()
	}
	
	fun formatHours(time: LocalTime?): String {
		return if(time == null) {
			""
		} else if(config.hour24.value) {
			time.toString()
		} else if(time.hour > 12) {
			"${time.hour - 12}:${time.minute} PM"
		} else if(time.hour == 0) {
			"12:${time.minute} AM"
		} else {
			"${time.toString()} AM"
		}
	}
	
}
