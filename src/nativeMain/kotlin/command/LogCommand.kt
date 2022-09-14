package command

import core.Color.green
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlin.math.abs

class LogCommand : ICommand {
	override val name = "LOG"
	override val args: List<String> = listOf("[days]")
	override val description: String = "Shows all clocked in and out entries for the day. Providing a days argument shows the log for that many days ago."
	override val shortDescription: String = "Shows a log of entries for today. Providing days shows the log for that many days ago."
	
	override fun execute(args: List<String>) {
		val daysArg = args.getOrNull(1)
		val days = if(daysArg == null) {
			0
		} else {
			try {
				abs(daysArg.toInt())
			} catch(nfe: NumberFormatException) {
				println(yellow("Must provide a whole number for days argument!"))
				return
			}
		}
		
		val timeEntries = TimeEntries.load(Util.todayMinus(days))
		if(timeEntries.entries.isEmpty()) {
			when(days) {
				0 -> println("No entries for today. Use 'timecard in' to clock in.")
				1 -> println("No entries for yesterday.")
				else -> println("No entries for $days days ago.")
			}
			return
		}
		
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