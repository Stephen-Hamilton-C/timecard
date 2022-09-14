package command

import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.baseName
import com.soywiz.korio.file.std.localVfs
import config.Configuration
import core.Color.cyan
import core.TimeEntries
import core.Util
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate

class CleanCommand : IAutoCommand {
	
	override val name: String = "CLEAN"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	private fun clean(automatic: Boolean) = runBlocking {
		val config = Configuration.load()
		var days = config.cleanInterval.days
		if(days == -1) {
			if(automatic) {
				return@runBlocking
			} else {
				days = 1
			}
		}
		val dataDir = localVfs(TimeEntries.dataDir)
		
		// I know Korio has VfsFile.listSimple(), but it's broken as it doesn't retain path information
		val timecardNames = dataDir.listNames().filter { it.isNotEmpty() }
		timecardNames.forEach { name ->
			val file: VfsFile = dataDir[name]
			if (file.baseName.startsWith("timecard_") && file.baseName.endsWith(".json")) {
				val dateString = file.baseName.substring(9, file.baseName.length - 5)
				try {
					val date = LocalDate.parse(dateString)
					
					if (date <= Util.todayMinus(days)) {
						file.delete()
						println(cyan("Removed old timecard file for $date"))
					}
				} catch (_: IllegalArgumentException) {
					// Likely a DateTimeFormatException from parsing a bad file name.
					// Just ignore it and move on.
				}
			}
		}
	}
	
	override fun execute(args: List<String>) = clean(false)
	override fun autoExecute() = clean(true)
	
}