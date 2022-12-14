package command

import core.Color.green
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util

class UndoCommand : ICommand {
	override val name = "UNDO"
	override val args: List<String> = listOf()
	override val description: String = "Removes the last entry made"
	override val shortDescription: String = description
	
	override fun execute(args: List<String>) {
		val timeEntries = TimeEntries.load()
		val lastTime = timeEntries.undo()
		if (lastTime == null) {
			println(yellow("No entries left to undo!"))
			println("Use 'timecard in' to clock in.")
		} else if(timeEntries.isClockedIn()) {
			println("Undo: Clock ${red("OUT")} at ${red(Util.formatHours(lastTime))}")
			println(green("Clocked IN since ${Util.formatHours(timeEntries.lastStartTime())}"))
		} else if(timeEntries.isClockedOut()) {
			println("Undo: Clock ${green("IN")} at ${green(Util.formatHours(lastTime))}")
			if(timeEntries.entries.isEmpty()) {
				println(red("Clocked OUT. No time log remains for today."))
			} else {
				println(red("Clocked OUT since ${Util.formatHours(timeEntries.lastEndTime())}"))
			}
		}
		
		// Don't waste time saving if nothing changed.
		if (lastTime != null) {
			timeEntries.save()
		}
	}
}
