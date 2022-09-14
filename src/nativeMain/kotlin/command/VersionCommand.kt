package command

class VersionCommand : ICommand {
	override val name: String = "VERSION"
	override val args: List<String> = listOf()
	override val description: String = "Outputs the current timecard version"
	override val shortDescription: String = description
	
	override fun execute(args: List<String>) {
		// TODO: This needs to be set in Gradle settings and somehow retrieved here
		println("timecard version 0.1.0")
	}
}