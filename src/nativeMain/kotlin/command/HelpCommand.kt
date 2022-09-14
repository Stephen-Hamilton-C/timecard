package command

import kotlin.system.exitProcess

class HelpCommand : ICommand {
	override val name = "HELP"
	override val args: List<String> = listOf("[command]")
	override val description: String = "Shows help for all commands, or detailed help for a specific command if one is provided."
	override val shortDescription: String = description
	
	/**
	 * Formats the command's name and arguments for display to the user
	 */
	private fun commandDisplay(command: ICommand): String {
		val commandArgs: String = try {
			if (command.args.isEmpty()) {
				// No args, don't add anything.
				""
			} else {
				// Show args
				" ${command.args.joinToString(" ")}"
			}
		} catch (nie: NotImplementedError) {
			// Command is still being worked on. Don't add anything.
			""
		}
		
		return "${command.name.lowercase()}$commandArgs"
	}
	
	override fun execute(args: List<String>) {
		if (args.size == 1) {
			// Show list of commands
			println("usage: timecard <command> [<args>]")
			for (command in CommandList.commands) {
				val shortDesc: String = try {
					command.shortDescription
				} catch (nie: NotImplementedError) {
					"No description available currently."
				}
				
				println("  ${commandDisplay(command)} - $shortDesc")
			}
		} else {
			// Show detailed help for a command
			val foundCommand = CommandList.commands.find { it.name == args[1].uppercase() }
			if (foundCommand == null) {
				println("No command by that name found. Use 'timecard help' for a list of commands.")
				exitProcess(1)
			}
			
			val desc: String = try {
				foundCommand.description
			} catch (nie: NotImplementedError) {
				// Command is still being worked on. Use this default description text.
				"No description available currently.\n" +
						"This is likely due to the command implementation not being complete.\n" +
						"Use this command with caution, especially if this is a snapshot build."
			}
			
			println("timecard ${commandDisplay(foundCommand)}:")
			println(desc)
		}
	}
}