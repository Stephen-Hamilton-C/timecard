package command

object CommandList {
	val commands: List<ICommand> = listOf (
		VersionCommand(),
		HelpCommand(),
		StatusCommand(),
		ClockInCommand(),
		ClockOutCommand(),
		LogCommand(),
		UndoCommand(),
		ConfigCommand(),
		CleanCommand(),
		FilesCommand(),
	).sortedBy { it.name }
}