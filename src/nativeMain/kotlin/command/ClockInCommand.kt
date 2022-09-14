package command

import ClockedInException
import core.Color.green
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime
import kotlin.system.exitProcess

class ClockInCommand : ClockCommand() {
	override val name = "IN"
	override val description: String = "Clocks you in. An offset can be provided in minutes, or at a specific time. (e.g. 'timecard in 15' will clock in 15 minutes ago, and 'timecard in 6:12' will clock you in at 6:12 AM. 24-hour time is supported as well.)"
	override val shortDescription: String = "Clocks you in. An offset can be provided in minutes, or at a specific time."
	
	override val invalidPastTimeMessage = "Time provided is before last clock out time! Use 'timecard log' to see when last clock out was."
	
	override fun clockExecute(timeEntries: TimeEntries, time: LocalTime) {
		try {
			timeEntries.clockIn(time)
			println("Clocked ${green("IN")} at ${green(Util.formatHours(time))}.")
		} catch(cie: ClockedInException) {
			println(yellow("Already clocked in! Use 'timecard out' to clock out, or use 'timecard undo' to remove last clock in."))
			exitProcess(1)
		}
	}
}