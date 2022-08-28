import command.CommandList
import command.IAutoCommand
import command.StatusCommand

fun main(args: Array<String>) {
    val command = if (args.isNotEmpty()) {
        CommandList.commands.find {
            it.name == args[0].uppercase()
        }
    } else {
        StatusCommand()
    }
    if (command == null) {
        println("Unknown command. Use 'timecard help' for a list of commands.")
        return
    }
    command.execute(args.toList())
    
    val autoCommands = CommandList.commands.filterIsInstance<IAutoCommand>()
    autoCommands.forEach {
        it.autoExecute()
    }
}
