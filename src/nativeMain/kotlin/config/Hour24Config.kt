package config

import BadConfigValueException

class Hour24Config : IConfig {
	override val name = "24_hour"
	override val description: String = "Determines if time is shown in 24-hour format."
	override val possibleValues: List<String> = BooleanOptions.values().map { it.name.uppercase() }
	
	override fun retrieveValue(): String = Configuration.load().hour24.toString()
	
	override fun setValue(userInput: String) {
		val parsedValue = userInput.trim().lowercase().toBooleanStrictOrNull() ?: throw BadConfigValueException()
		val value = BooleanOptions.from(parsedValue)
		val config = Configuration.load()
		config.hour24 = value
		config.save()
	}
}