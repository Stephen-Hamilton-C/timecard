package command

class ConfigCommand : ICommand {
	override val name = "CONFIG"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	override fun execute(args: List<String>) {
		// Get IConfig from args[1]
		// If args[2] is null, simply display the current value and possible values
		// If args[2] is not null, try to set the config to args[2]
		// If a BadConfigValueException is thrown, report to the user all possible values
	}
	
}