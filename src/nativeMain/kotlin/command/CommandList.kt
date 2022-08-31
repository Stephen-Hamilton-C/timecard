package command

object CommandList {
	val commands: List<ICommand>
		get() = _commands
	private val _commands = listOf<ICommand>(
		StatusCommand(),
		ClockInCommand(),
		ClockOutCommand(),
		LogCommand(),
		UndoCommand(),
	)
}