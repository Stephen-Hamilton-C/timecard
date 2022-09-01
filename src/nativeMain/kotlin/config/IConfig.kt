package config

interface IConfig {
	val name: String
	val possibleValues: List<String>
	fun set(userInput: String)
}