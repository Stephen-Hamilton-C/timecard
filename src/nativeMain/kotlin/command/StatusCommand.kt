package command

import config.Configuration
import config.TimeFormatOptions
import core.Color.green
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime
import kotlin.math.roundToInt

class StatusCommand : ICommand {
	override val name: String = "STATUS"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	private fun addS(number: Int): String {
		return if(number == 1) {
			""
		} else {
			"s"
		}
	}
	
	private fun formatTime(time: LocalTime?): String {
		if(time == null) return ""
		val config = Configuration.load()
		return when(config.timeFormat) {
			TimeFormatOptions.WRITTEN -> {
				val hourS = addS(time.hour)
				val minuteS = addS(time.minute)
				"${time.hour} hour$hourS ${time.minute} minute$minuteS"
			}
			TimeFormatOptions.WRITTEN_SHORT -> {
				val hourS = addS(time.hour)
				val minuteS = addS(time.minute)
				"${time.hour} hr$hourS ${time.minute} min$minuteS"
			}
			TimeFormatOptions.ISO -> {
				"${time.hour}:${time.minute}"
			}
			TimeFormatOptions.QUARTER_HOUR -> {
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