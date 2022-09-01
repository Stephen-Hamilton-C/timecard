package command

object CommandList {
	val commands: List<ICommand> = listOf (
		HelpCommand(),
		StatusCommand(),
		ClockInCommand(),
		ClockOutCommand(),
		LogCommand(),
		UndoCommand(),
		ConfigCommand(),
		CleanCommand(),
	)
}