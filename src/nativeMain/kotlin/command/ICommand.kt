package command

interface ICommand {
	val name: String
	val description: String
	val shortDescription: String
	
	fun execute(args: List<String>)
}