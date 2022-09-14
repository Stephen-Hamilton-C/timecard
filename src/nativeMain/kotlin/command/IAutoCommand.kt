package command

interface IAutoCommand : ICommand {
	/**
	 * Code to automatically run after a command is run
	 */
	fun autoExecute()
}