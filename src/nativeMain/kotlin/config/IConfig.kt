package config

interface IConfig {
	val name: String
	val description: String
	val possibleValues: List<String>
	fun retrieveValue(): String
	fun setValue(userInput: String)
}