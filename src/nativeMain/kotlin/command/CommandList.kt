package command

object CommandList {
	val commands: List<ICommand>
		get() = _commands
	private val _commands = listOf<ICommand>(
		HelpCommand(),
		StatusCommand(),
		ClockInCommand(),
		ClockOutCommand(),
		LogCommand(),
		UndoCommand(),
		CleanCommand(),
	)
}