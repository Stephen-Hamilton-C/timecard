package command

import Color.green
import Color.red
import Color.yellow
import core.TimeEntries

class UndoCommand : ICommand {
	override val name = "UNDO"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override fun execute(args: List<String>) {
		val timeEntries = TimeEntries.load()
		val lastTime = timeEntries.undo()
		if (lastTime == null) {
			println(yellow("No entries left to undo!"))
			println("Use 'timecard in' to clock in.")
		} else if(timeEntries.isClockedIn()) {
			println("Undo: Clock ${red("out")} at $lastTime")
			println(green("Clocked in since ${timeEntries.lastStartTime()}"))
		} else if(timeEntries.isClockedOut()) {
			println("Undo: Clock ${green("in")} at $lastTime")
			if(timeEntries.entries.isEmpty()) {
				println(red("Clocked out. No time log remains for today."))
			} else {
				println(red("Clocked out since ${timeEntries.lastEndTime()}"))
			}
		}
		
		// Don't waste time saving if nothing changed.
		if (lastTime != null) {
			timeEntries.save()
		}
	}
}