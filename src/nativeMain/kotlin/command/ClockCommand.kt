package command

import Color.yellow
import InvalidTimeException
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

abstract class ClockCommand : ICommand {
	
	abstract val invalidTimeMessage: String
	
	private fun unknownArgError(): Nothing {
		println(yellow("Unknown argument. Must be an integer or a specified time."))
		exitProcess(1)
	}
	
	private fun parseTimeString(timeString: String): LocalTime {
		val splitTime = timeString.split(':')
		if(splitTime.size != 2) unknownArgError()
		
		try {
			var hour: Int = splitTime[0].toInt()
			val minute: Int = if (splitTime[1].endsWith("AM") || splitTime[1].endsWith("PM")) {
				val hour12Indicator = splitTime[1].substring(splitTime[1].lastIndex - 1)
				if (hour < 12 && hour12Indicator == "PM") {
					hour += 12
				} else if(hour == 12 && hour12Indicator == "AM") {
					hour = 0
				}
				
				splitTime[1].substring(0, splitTime[1].lastIndex - 2).toInt()
			} else {
				splitTime[1].toInt()
			}
			return LocalTime(hour, minute)
		} catch (nfe: NumberFormatException) {
			unknownArgError()
		}
	}
	
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
				invalidTimeMessage
			}
			println(yellow(eMessage))
			exitProcess(1)
		}
	}
}