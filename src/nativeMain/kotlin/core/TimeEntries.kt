package core

import ClockedInException
import ClockedOutException
import InvalidTimeException
import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Manages operations and data for all entries.
 */
@Serializable
class TimeEntries(
	@SerialName("entries")
	private val _entries: MutableList<TimeEntry> = mutableListOf()
) {
	/**
	 * All entries for the day.
	 * Modification of these entries should be done with methods on this object
	 * rather than through this variable.
	 */
	val entries: List<TimeEntry>
		get() = _entries.toList()
	
	init {
		// Ensure all entries make sense
		sanityCheck()
	}
	
	companion object {
		/**
		 * The directory where all timecard files are stored
		 */
		val dataDir = AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C")
		
		/**
		 * Creates a TimeEntries from a timecard file found for the date provided.
		 * If no date is provided, will default to today's timecard.
		 */
		fun load(date: LocalDate = Util.TODAY) = runBlocking {
			val manager = ClassFileManager(localVfs(dataDir)["timecard_$date.json"])
			return@runBlocking manager.load(::TimeEntries)
		}
	}
	
	/**
	 * Saves this TimeEntries to a file for the date provided.
	 * If no date is provided, will default to the current date.
	 */
	fun save(date: LocalDate = Util.TODAY) = runBlocking {
		sanityCheck()
		val manager = ClassFileManager(localVfs(dataDir)["timecard_$date.json"])
		manager.save(this@TimeEntries)
	}
	
	/**
	 * Checks that the _entries list is valid.
	 * @throws IllegalStateException if the _entries list is not valid.
	 */
	private fun sanityCheck() {
		val times = mutableListOf<LocalTime>()
		for (entry in _entries) {
			if (entry.endTime == null && entry != _entries.last()) {
				throw IllegalStateException("A null endTime was found in the middle of entries!")
			}
			times.add(entry.startTime)
			if (entry.endTime != null) {
				times.add(entry.endTime!!)
			}
		}
		
		var lastTime: LocalTime? = null
		for (time in times) {
			if(lastTime != null && lastTime > time) {
				throw IllegalStateException("Entries cannot overlap!")
			}
			lastTime = time
		}
	}
	
	private fun difference(earlierTime: LocalTime, laterTime: LocalTime): LocalTime {
		var minute = laterTime.minute - earlierTime.minute
		var hour = laterTime.hour - earlierTime.hour
		
		if(minute < 0) {
			minute += 60
			hour--
		}
		
		return LocalTime(hour, minute)
	}
	
	private fun add(time1: LocalTime, time2: LocalTime): LocalTime {
		var hours = time1.hour + time2.hour
		var minutes = time1.minute + time2.minute
		
		if(minutes >= 60) {
			minutes -= 60
			hours++
		}
		
		return LocalTime(hours, minutes)
	}
	
	override fun equals(other: Any?): Boolean {
		if (other == null) return false
		if (other is TimeEntries) {
			return this.entries == other.entries
		}
		return false
	}
	
	override fun hashCode(): Int {
		return _entries.hashCode()
	}
	
	fun isClockedIn(): Boolean = _entries.isNotEmpty() && _entries.last().endTime == null
	fun isClockedOut(): Boolean = !isClockedIn()
	fun lastStartTime(): LocalTime? = _entries.lastOrNull()?.startTime
	fun lastEndTime(): LocalTime? = _entries.lastOrNull()?.endTime
	
	/**
	 * Adds an entry with the start time at the time provided.
	 * If no time provided, the current time is used.
	 * @precondition Cannot be clocked in
	 * @precondition Time cannot be before last clock out time
	 * @throws ClockedInException If already clocked in when method is called.
	 * @throws InvalidTimeException If the time provided is before the last clock out time or after the current time.
	 */
	fun clockIn(time: LocalTime = Util.NOW) {
		if (isClockedIn()) throw ClockedInException()
		// We are clocked in, so endTime must exist, otherwise this TimeEntries has lost sanity
		if (_entries.isNotEmpty() && time < _entries.last().endTime!!)
			throw InvalidTimeException(false)
		if (time > Util.NOW)
			throw InvalidTimeException(true)
		
		_entries.add(TimeEntry(time))
	}
	
	/**
	 * Adds an end time to the last entry at the time provided.
	 * If no time provided, the current time is used.
	 * @precondition Cannot be clocked out
	 * @precondition Time cannot be before last clock in time
	 * @throws ClockedOutException If already clocked out when method is called.
	 * @throws InvalidTimeException If the time provided is before the last clock in time or after the current time.
	 */
	fun clockOut(time: LocalTime = Util.NOW) {
		if(isClockedOut()) throw ClockedOutException()
		if (time < _entries.last().startTime)
			throw InvalidTimeException(false)
		if (time > Util.NOW)
			throw InvalidTimeException(true)
		
		_entries.last().endTime = time
	}
	
	/**
	 * Undoes last clock in or out operation.
	 * @return The time that was removed or null if there is nothing left to undo.
	 */
	fun undo(): LocalTime? {
		if (_entries.isEmpty()) return null
		
		val lastTime: LocalTime
		if (isClockedIn()) {
			lastTime = lastStartTime()!!
			_entries.removeLast()
		} else {
			lastTime = lastEndTime()!!
			_entries.last().endTime = null
		}
		
		return lastTime
	}
	
	/**
	 * Calculates the total time the user has been clocked in
	 * @return A LocalTime with hours and minutes worked
	 */
	fun calculateWorkedTime(): LocalTime {
		var sum = LocalTime(0, 0)
		
		for(entry in _entries) {
			val endTime = entry.endTime ?: Util.NOW
			val diff = difference(entry.startTime, endTime)
			sum = add(sum, diff)
		}
		
		return sum
	}
	
	/**
	 * Calculates the total time the user has been clocked out
	 * @return A LocalTime with hours and minutes worked
	 */
	fun calculateBreakTime(): LocalTime {
		var sum = LocalTime(0, 0)
		
		for((i, entry) in _entries.withIndex()) {
			if(entry.endTime == null) break
			
			val nextTime = if(i == _entries.lastIndex && isClockedOut()) {
				Util.NOW
			} else {
				_entries[i + 1].startTime
			}
			val diff = difference(entry.endTime!!, nextTime)
			sum = add(sum, diff)
		}
		
		return sum
	}
	
}