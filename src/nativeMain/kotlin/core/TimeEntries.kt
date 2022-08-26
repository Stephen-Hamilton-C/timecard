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
		
		suspend fun load(date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): TimeEntries {
			val manager = ClassFileManager(localVfs(_dataDir)["timecard_$date.json"])
			return manager.load(::TimeEntries)
		}
	}
	
	suspend fun save(date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())) {
		val manager = ClassFileManager(localVfs(_dataDir)["timecard_$date.json"])
		manager.save(this)
	}
}