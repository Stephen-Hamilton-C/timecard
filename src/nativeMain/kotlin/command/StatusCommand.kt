package command

import Color.green
import Color.red
import Color.yellow
import core.TimeEntries

class StatusCommand : ICommand {
	override val name: String = "STATUS"
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override fun execute(args: List<String>) {
		val timeEntries = TimeEntries.load() // Leave this here so we can specify date later
		if (timeEntries.entries.isEmpty()) {
			println(yellow("You haven't clocked in yet today! Use 'timecard in' to clock in.")) // Add different message for other dates
		} else if (timeEntries.isClockedIn()) {
			println("Clocked ${green("in")} since ${green(timeEntries.lastStartTime())}.")
		} else {
			println("Clocked ${red("out")} since ${red(timeEntries.lastEndTime())}.")
		}
	}
}