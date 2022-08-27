package core

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TimeEntries {
	val entries: List<TimeEntry>
		get() = _entries.toList()
	@SerialName("entries")
	private val _entries: MutableList<TimeEntry> = mutableListOf()
	
	companion object {
		private val _dataDir = AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C")
		
		fun load(date: LocalDate = Util.TODAY) = runBlocking {
			val manager = ClassFileManager(localVfs(_dataDir)["timecard_$date.json"])
			return@runBlocking manager.load(::TimeEntries)
		}
	}
	
	fun save(date: LocalDate = Util.TODAY) = runBlocking {
		// Integrity check
		for (entry in _entries) {
			if (entry.endTime == null && entry != _entries.last()) {
				throw IllegalStateException("A null endTime was found in the middle of entries!")
			}
		}
		
		val manager = ClassFileManager(localVfs(_dataDir)["timecard_$date.json"])
		manager.save(this@TimeEntries)
	}
	
	fun isClockedIn(): Boolean = _entries.size > 0 && _entries.last().endTime == null
	fun isClockedOut(): Boolean = !isClockedIn()
	
	/**
	 * @precondition Cannot be clocked in
	 * @precondition Time cannot be before last clock out time
	 */
	fun clockIn(time: LocalTime = Util.NOW) {
		if (isClockedIn()) throw IllegalStateException("Already clocked in!")
		if (_entries.size > 0 && time < _entries.last().endTime!!)
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
		if (_entries.size == 0) throw IllegalStateException("Nothing left to undo!")
		
		if (isClockedIn()) {
			_entries.removeLast()
		} else {
			_entries.last().endTime = null
		}
	}
	
}