package command

import InvalidTimeException
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

abstract class ClockCommand : ICommand {
	
	override val args: List<String> = listOf("[offset/time]")
	
	/**
	 * The message to display when the user tries to input a time before the last entry.
	 */
	abstract val invalidPastTimeMessage: String
	
	/**
	 * Displays a message to the user that arguments are wrong and exits with code 1
	 */
	private fun unknownArgError(): Nothing {
		println(yellow("Unknown argument. Must be an integer or a specified time."))
		exitProcess(1)
	}
	
	/**
	 * Converts user inputted time into a LocalTime.
	 */
	fun parseTimeString(timeInput: String): LocalTime {
		val splitTime = timeInput.uppercase().split(':')
		if(splitTime.size != 2) unknownArgError()
		
		try {
			var hour: Int = splitTime[0].toInt()
			val minute: Int = if (splitTime[1].endsWith("AM") || splitTime[1].endsWith("PM")) {
				val hour12Indicator = splitTime[1].substring(splitTime[1].lastIndex - 1)
				if (hour < 12 && hour12Indicator.endsWith("PM")) {
					hour += 12
				} else if(hour == 12 && hour12Indicator.endsWith("AM")) {
					hour = 0
				}
				// If user provides AM/PM mixed with 24-hour time,
				// 12-hour will be ignored and will parse as 24-hour
				// e.g. 17:31 PM should parse to LocalTime(17, 31)
				
				splitTime[1].substring(0..1).toInt()
			} else {
				splitTime[1].toInt()
			}
			return LocalTime(hour, minute)
		} catch (nfe: NumberFormatException) {
			unknownArgError()
		}
	}
	
	/**
	 * Gets a LocalTime object from the command arguments
	 */
	private fun getTime(args: List<String>): LocalTime {
		val arg: String? = if(args.size >= 3) {
			"${args[1]} ${args[2]}"
		} else {
			args.getOrNull(1)
		}
		
		return if (arg == null) {
			Util.NOW
		} else {
			val offset = arg.toIntOrNull()?.absoluteValue
			if(offset == null) {
				// Parse time
				parseTimeString(arg)
			} else {
				Util.nowMinus(offset)
			}
		}
	}
	
	/**
	 * Performs actions specific to clocking in or out.
	 */
	abstract fun clockExecute(timeEntries: TimeEntries, time: LocalTime)
	
	override fun execute(args: List<String>) {
		try {
			val timeEntries = TimeEntries.load()
			clockExecute(timeEntries, getTime(args))
			timeEntries.save()
		} catch (ite: InvalidTimeException) {
			val eMessage = if(ite.afterNow) {
				"Given time is after the current time! Choose a time before now or don't specify a time for the current time."
			} else {
				invalidPastTimeMessage
			}
			println(yellow(eMessage))
			exitProcess(1)
		}
	}
}