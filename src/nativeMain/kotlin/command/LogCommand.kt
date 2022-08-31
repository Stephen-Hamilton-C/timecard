package command

import Color.green
import Color.red
import core.TimeEntries

class LogCommand : ICommand {
	override val name = "LOG"
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
		
		for(entry in timeEntries.entries) {
			println("Clocked ${green("IN")} at ${green(entry.startTime)}")
			if(entry.endTime != null) {
				println("Clocked ${red("OUT")} at ${red(entry.endTime)}")
			}
		}
		println()
	}
	
}