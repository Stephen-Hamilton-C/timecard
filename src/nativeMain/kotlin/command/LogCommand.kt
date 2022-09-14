package command

import core.Color.green
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlin.math.abs
import kotlin.system.exitProcess

class LogCommand : ICommand {
	override val name = "LOG"
	override val args: List<String> = listOf("[days]")
	override val description: String = "Shows all clocked in and out entries for the day. Providing a days argument shows the log for that many days ago."
	override val shortDescription: String = "Shows a log of entries for today. Providing days shows the log for that many days ago."
	
	private fun getDaysFromArg(daysArg: String?): Int {
		return if(daysArg == null) {
			// No arg provided, assume today
			0
		} else {
			try {
				// Take absolute value because users are users
				abs(daysArg.toInt())
			} catch(nfe: NumberFormatException) {
				// User provided non-integer input
				println(yellow("Must provide a whole number for days argument!"))
				exitProcess(1)
			}
		}
	}
	
	override fun execute(args: List<String>) {
		val days = getDaysFromArg(args.getOrNull(1))
		val timeEntries = TimeEntries.load(Util.todayMinus(days))
		
		// Show message if no entries were found
		if(timeEntries.entries.isEmpty()) {
			when(days) {
				0 -> println("No entries for today. Use 'timecard in' to clock in.")
				1 -> println("No entries for yesterday.")
				else -> println("No entries for $days days ago.")
			}
			return
		}
		
		// Indicate which day these entries are for
		when(days) {
			0 -> println("Entries for today:")
			1 -> println("Entries for yesterday:")
			else -> println("Entries for $days days ago:")
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