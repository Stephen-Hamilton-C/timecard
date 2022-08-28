package core

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TimeEntries(
	@SerialName("entries")
	private val _entries: MutableList<TimeEntry> = mutableListOf()
) {
	val entries: List<TimeEntry>
		get() = _entries.toList()
	
	init {
		sanityCheck()
	}
	
	companion object {
		private val _dataDir = AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C")
		
		fun load(date: LocalDate = Util.TODAY) = runBlocking {
			val manager = ClassFileManager(localVfs(_dataDir)["timecard_$date.json"])
			return@runBlocking manager.load(::TimeEntries)
		}
	}
	
	fun save(date: LocalDate = Util.TODAY) = runBlocking {
		sanityCheck()
		val manager = ClassFileManager(localVfs(_dataDir)["timecard_$date.json"])
		manager.save(this@TimeEntries)
	}
	
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
	
	/**
	 * @precondition Cannot be clocked in
	 * @precondition Time cannot be before last clock out time
	 */
	fun clockIn(time: LocalTime = Util.NOW) {
		if (isClockedIn()) throw IllegalStateException("Already clocked in!")
		// We are clocked in, so endTime must exist, otherwise this TimeEntries has lost sanity
		if (_entries.isNotEmpty() && time < _entries.last().endTime!!)
			throw IllegalStateException("Clock in time is before last clock out time!")
		
		_entries.add(TimeEntry(time))
	}
	
	/**
	 * @precondition Cannot be clocked out
	 * @precondition Time cannot be before last clock in time
	 */
	fun clockOut(time: LocalTime = Util.NOW) {
		if(isClockedOut()) throw IllegalStateException("Already clocked out!")
		if (time < _entries.last().startTime)
			throw IllegalStateException("Clock out time is before last clock in time!")
		
		_entries.last().endTime = time
	}
	
	fun undo() {
		if (_entries.isEmpty()) throw IllegalStateException("Nothing left to undo!")
		
		if (isClockedIn()) {
			_entries.removeLast()
		} else {
			_entries.last().endTime = null
		}
	}
	
}