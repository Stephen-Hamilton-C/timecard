package command

import ClockedOutException
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime
import kotlin.system.exitProcess

class ClockOutCommand : ClockCommand() {
	override val name = "OUT"
	override val description: String = "Clocks you out. An offset can be provided in minutes, or at a specific time. (e.g. 'timecard out 15' will clock out 15 minutes ago, and 'timecard out 5:31 PM' will clock you out at 5:31 PM. 24-hour time is supported as well.)"
	override val shortDescription: String = "Clocks you out. An offset can be provided in minutes, or at a specific time."
	
	override val invalidPastTimeMessage = "Time provided is before last clock in time! Use 'timecard log' to see when last clock in was."
	
	override fun clockExecute(timeEntries: TimeEntries, time: LocalTime) {
		try {
			timeEntries.clockOut(time)
			println("Clocked ${red("OUT")} at ${red(Util.formatHours(time))}.")
		} catch(cie: ClockedOutException) {
			println(yellow("Already clocked out! Use 'timecard in' to clock in, or use 'timecard undo' to remove last clock out."))
			exitProcess(1)
		}
	}
}