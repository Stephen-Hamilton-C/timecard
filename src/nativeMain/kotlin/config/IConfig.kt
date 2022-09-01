package config

interface IConfig {
	val name: String
	val possibleValues: List<String>
	fun retrieveValue(): String
	fun setValue(userInput: String)
}