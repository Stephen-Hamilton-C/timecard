package command

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
			println("You haven't clocked in yet today! Use 'timecard in' to clock in.") // Add different message for other dates
		} else if (timeEntries.isClockedIn()) {
			println("Clocked in since ${timeEntries.lastStartTime()}.")
		} else {
			println("Clocked out since ${timeEntries.lastEndTime()}.")
		}
	}
}