package config

interface IConfig {
	val name: String
	val possibleValues: List<String>
	var value: String
}