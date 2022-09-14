package config

import BadConfigValueException

class ColorConfig : IConfig {
	override val name = "color"
	override val description = "Determines if text should be colorful. Some terminals may not support this."
	override val possibleValues: List<String> = BooleanOptions.values().map { it.name.uppercase() }
	
	override fun retrieveValue(): String = Configuration.load().color.toString()
	
	override fun setValue(userInput: String) {
		val parsedValue = userInput.trim().lowercase().toBooleanStrictOrNull() ?: throw BadConfigValueException()
		val value = BooleanOptions.from(parsedValue)
		val config = Configuration.load()
		config.color = value
		config.save()
	}
}