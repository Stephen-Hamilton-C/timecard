package command

import core.Color.green
import core.Color.red
import core.Color.yellow
import core.TimeEntries
import core.Util
import kotlinx.datetime.LocalTime

class StatusCommand : ICommand {
	override val name: String = "STATUS"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	private fun calculateWorkedTime(timeEntries: TimeEntries): LocalTime {
		var hourSum = 0
		var minuteSum = 0
		
		for(entry in timeEntries.entries) {
			val endTime = entry.endTime ?: Util.NOW
			val diff = Util.difference(entry.startTime, endTime)
			hourSum += diff.hour
			minuteSum += diff.minute
			if(minuteSum >= 60) {
				minuteSum -= 60
				hourSum += 1
			}
		}
		
		return LocalTime(hourSum, minuteSum)
	}
	
	private fun calculateBreakTime(timeEntries: TimeEntries): LocalTime {
		var hourSum = 0
		var minuteSum = 0
		
		for((i, entry) in timeEntries.entries.withIndex()) {
			if(entry.endTime == null) break
			
			val diff = if(i == timeEntries.entries.lastIndex && entry.endTime != null) {
				Util.difference(entry.endTime!!, Util.NOW)
			} else {
				Util.difference(entry.endTime!!, timeEntries.entries[i + 1].startTime)
			}
			hourSum += diff.hour
			minuteSum += diff.minute
			if(minuteSum >= 60) {
				minuteSum -= 60
				hourSum += 1
			}
		}
		
		return LocalTime(hourSum, minuteSum)
	}
	
	override fun execute(args: List<String>) {
		val timeEntries = TimeEntries.load()
		if (timeEntries.entries.isEmpty()) {
			println(yellow("You haven't clocked in yet today! Use 'timecard in' to clock in."))
			return
		} else if (timeEntries.isClockedIn()) {
			println("Clocked ${green("IN")} since ${green(timeEntries.lastStartTime())}.")
		} else {
			println("Clocked ${red("OUT")} since ${red(timeEntries.lastEndTime())}.")
		}
		// Print worked time
		println("Worked for ${calculateWorkedTime(timeEntries)}")
		// Print time on break
		println("On break for ${calculateBreakTime(timeEntries)}")
	}
}