package command

import core.Color.green
import core.Color.red
import core.Color.yellow
import core.TimeEntries

class StatusCommand : ICommand {
	override val name: String = "STATUS"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override fun execute(args: List<String>) {
		val timeEntries = TimeEntries.load()
		if (timeEntries.entries.isEmpty()) {
			println(yellow("You haven't clocked in yet today! Use 'timecard in' to clock in."))
		} else if (timeEntries.isClockedIn()) {
			println("Clocked ${green("IN")} since ${green(timeEntries.lastStartTime())}.")
		} else {
			println("Clocked ${red("OUT")} since ${red(timeEntries.lastEndTime())}.")
		}
	}
}