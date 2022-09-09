package config

import BadConfigValueException

enum class ColorOptions(val value: Boolean) {
	TRUE(true),
	FALSE(false);
	
	companion object {
		fun from(value: Boolean): ColorOptions {
			return if (value) {
				TRUE
			} else {
				FALSE
			}
		}
	}
}

class ColorConfig : IConfig {
	override val name = "color"
	override val description = "Determines if text should be colorful. Some terminals may not support this."
	override val possibleValues: List<String> = ColorOptions.values().map { it.name.uppercase() }
	override fun retrieveValue(): String = Configuration.load().color.toString()
	override fun setValue(userInput: String) {
		val parsedValue = userInput.trim().lowercase().toBooleanStrictOrNull() ?: throw BadConfigValueException()
		val value = ColorOptions.from(parsedValue)
		val config = Configuration.load()
		config.color = value
		config.save()
	}
}