package command

import Color.cyan
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.baseName
import com.soywiz.korio.file.std.localVfs
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
	
	override fun execute(args: List<String>) {
		autoExecute()
	}
	
	override fun autoExecute() = runBlocking {
		val dataDir = localVfs(TimeEntries.dataDir)
		
		// I know Korio has VfsFile.listSimple(), but it's broken as it doesn't retain path information
		val timecardNames = dataDir.listNames().filter { it.isNotEmpty() }
		timecardNames.forEach { name ->
			val file: VfsFile = dataDir[name]
			if (file.baseName.startsWith("timecard_") && file.baseName.endsWith(".json")) {
				val dateString = file.baseName.substring(9, file.baseName.length - 5)
				try {
					val date = LocalDate.parse(dateString)
					
					// TODO: Make config for this
					if (date < Util.todayMinus(7)) {
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
	
}