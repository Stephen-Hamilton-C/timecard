package command

import config.Configuration
import core.Color.cyan
import core.TimeEntries

class FilesCommand : ICommand {
	override val name = "FILES"
	override val args: List<String> = listOf()
	override val description: String = "Shows where timecard and config files are located at."
	override val shortDescription: String = description
	
	override fun execute(args: List<String>) {
		println("Config files are located at ${cyan(Configuration.configDir)}")
		println("Data files are located at ${cyan(TimeEntries.dataDir)}")
	}
	
}