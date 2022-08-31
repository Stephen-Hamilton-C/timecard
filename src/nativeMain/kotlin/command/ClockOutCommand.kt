package command

import ClockedOutException
import Color.red
import Color.yellow
import core.TimeEntries
import kotlinx.datetime.LocalTime
import kotlin.system.exitProcess

class ClockOutCommand : ClockCommand() {
	override val name = "OUT"
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override val invalidPastTimeMessage = "Time provided is before last clock in time! Use 'timecard log' to see when last clock in was."
	
	override fun clockExecute(timeEntries: TimeEntries, time: LocalTime) {
		try {
			timeEntries.clockOut(time)
			println("Clocked ${red("OUT")} at ${red(time)}.")
		} catch(cie: ClockedOutException) {
			println(yellow("Already clocked out! Use 'timecard in' to clock in, or use 'timecard undo' to remove last clock out."))
			exitProcess(1)
		}
	}
}