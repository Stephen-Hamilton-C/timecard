package command

import Color.cyan
import config.Configuration
import core.TimeEntries

class FilesCommand : ICommand {
	override val name = "FILES"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override fun execute(args: List<String>) {
		println("Config files are located at ${cyan(Configuration.configDir)}")
		println("Data files are located at ${cyan(TimeEntries.dataDir)}")
	}
	
}