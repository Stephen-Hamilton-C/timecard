package command

import core.Color.green
import core.Color.red
import core.TimeEntries
import core.Util

class LogCommand : ICommand {
	override val name = "LOG"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override fun execute(args: List<String>) {
		// TODO: Take in one argument to load a Timecard from NUM days ago
		val timeEntries = TimeEntries.load()
		if(timeEntries.entries.isEmpty()) {
			println("No entries for today. Use 'timecard in' to clock in.")
			return
		}
		
		// Print out every entry
		for(entry in timeEntries.entries) {
			println("Clocked ${green("IN")} at ${green(Util.formatHours(entry.startTime))}")
			if(entry.endTime != null) {
				println("Clocked ${red("OUT")} at ${red(Util.formatHours(entry.endTime))}")
			}
		}
		println()
	}
	
}