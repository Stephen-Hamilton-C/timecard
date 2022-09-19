package command

import VERSION

class VersionCommand : ICommand {
	override val name: String = "VERSION"
	override val args: List<String> = listOf()
	override val description: String = "Outputs the current timecard version"
	override val shortDescription: String = description
	
	override fun execute(args: List<String>) {
		println("timecard version $VERSION")
	}
}