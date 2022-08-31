package command

interface ICommand {
	/**
	 * What the user must type to execute this command.
	 */
	val name: String
	
	/**
	 * The arguments that are shown to the user
	 * when viewing this command in the help list.
	 */
	val args: List<String>
	
	/**
	 * A detailed description that may be several lines long.
	 * Used when help is activated on this command.
	 */
	val description: String
	
	/**
	 * A brief summary of what the command does.
	 * Should only be one line long.
	 * Used in the list of commands when `timecard help` is run.
	 */
	val shortDescription: String
	
	/**
	 * Code to run when the user activates this command.
	 */
	fun execute(args: List<String>)
}