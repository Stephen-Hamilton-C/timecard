package command

import ClockedInException
import Color.green
import Color.yellow
import core.TimeEntries
import kotlinx.datetime.LocalTime
import kotlin.system.exitProcess

class ClockInCommand : ClockCommand() {
	override val name = "IN"
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override val invalidTimeMessage = "Time provided is before last clock out time! Use 'timecard log' to see when last clock out was."
	
	override fun clockExecute(timeEntries: TimeEntries, time: LocalTime) {
		try {
			timeEntries.clockIn(time)
			println("Clocked ${green("IN")} at ${green(time)}.")
		} catch(cie: ClockedInException) {
			println(yellow("Already clocked in! Use 'timecard out' to clock out, or use 'timecard undo' to remove last clock in."))
			exitProcess(1)
		}
	}
}