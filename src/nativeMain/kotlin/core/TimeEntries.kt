package core

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TimeEntries {
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
}