package command

import config.Configuration
import config.TimeFormatOptions
import core.Color.green
import core.Color.magenta
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime
import kotlin.math.roundToInt

class StatusCommand : ICommand {
	override val name: String = "STATUS"
	override val args: List<String> = listOf()
	override val description: String = "Shows whether you are clocked in or out and how long you have worked and been on break. Use the ${magenta("time_format")} config to change how this is shown."
	override val shortDescription: String = "Shows whether you are clocked in or out and how long you have worked and been on break."
	
	/**
	 * Returns an s if the number is not 1
	 */
	private fun addS(number: Int): String {
		return if(number == 1) {
			""
		} else {
			"s"
		}
	}
	
	/**
	 * Formats the time according to the time_format config
	 */
	private fun formatTime(time: LocalTime?): String {
		if(time == null) return ""
		val config = Configuration.load()
		return when(config.timeFormat) {
			TimeFormatOptions.WRITTEN -> {
				// 6 hours, 12 minutes
				val hourS = addS(time.hour)
				val minuteS = addS(time.minute)
				"${time.hour} hour$hourS, ${time.minute} minute$minuteS"
			}
			TimeFormatOptions.WRITTEN_SHORT -> {
				// 6 hrs, 12 mins
				val hourS = addS(time.hour)
				val minuteS = addS(time.minute)
				"${time.hour} hr$hourS, ${time.minute} min$minuteS"
			}
			TimeFormatOptions.ISO -> {
				// 6:12
				"${time.hour}:${time.minute}"
			}
			TimeFormatOptions.QUARTER_HOUR -> {
				// 6.25 hours
				val quarterHour: Double = time.hour + ((time.minute / 15.0).roundToInt() * 15.0) / 60.0
				"$quarterHour hours"
			}
		}
	}
	
	override fun execute(args: List<String>) {
		val timeEntries = TimeEntries.load()
		if (timeEntries.entries.isEmpty()) {
			println(yellow("You haven't clocked in yet today! Use 'timecard in' to clock in."))
			return
		} else if (timeEntries.isClockedIn()) {
			println("Clocked ${green("IN")} since ${green(Util.formatHours(timeEntries.lastStartTime()))}.")
		} else {
			println("Clocked ${red("OUT")} since ${red(Util.formatHours(timeEntries.lastEndTime()))}.")
		}
		println("Worked for ${formatTime(timeEntries.calculateWorkedTime())}")
		println("On break for ${formatTime(timeEntries.calculateBreakTime())}")
	}
}