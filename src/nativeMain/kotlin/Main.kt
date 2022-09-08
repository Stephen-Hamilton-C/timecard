import core.Color.yellow
import command.CommandList
import command.IAutoCommand
import command.StatusCommand
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // Default to STATUS if no command is given
    val command = if (args.isNotEmpty()) {
        CommandList.commands.find {
            it.name == args[0].uppercase()
        }
    } else {
        StatusCommand()
    }
    
    // No command found
    if (command == null) {
        println(yellow("Unknown command. Use 'timecard help' for a list of commands."))
        exitProcess(1)
    }
    
    command.execute(args.toList())
    
    // AutoExecute all IAutoCommands
    val autoCommands = CommandList.commands.filterIsInstance<IAutoCommand>()
    autoCommands.forEach {
        it.autoExecute()
    }
}
