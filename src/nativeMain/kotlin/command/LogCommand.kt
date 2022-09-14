package command

import core.Color.green
import core.Color.red
import core.TimeEntries
import core.Util

class LogCommand : ICommand {
	override val name = "LOG"
	override val args: List<String> = listOf("[days]")
	override val description: String = "Shows all clocked in and out entries for the day. Providing a days argument shows the log for that many days ago."
	override val shortDescription: String = "Shows a log of entries for today. Providing days shows the log for that many days ago."
	
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