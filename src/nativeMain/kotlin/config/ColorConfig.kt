package config

import BadConfigValueException

enum class ColorOptions(val value: Boolean) {
	TRUE(true),
	FALSE(false);
	
	companion object {
		fun from(value: Boolean): ColorOptions {
			return if(value) {
				TRUE
			} else {
				FALSE
			}
		}
	}
}

class ColorConfig : IConfig {
	override val name = "COLOR"
	override val possibleValues: List<String> = ColorOptions.values().map { it.name.uppercase() }
	
	override fun set(userInput: String) {
		val parsedValue = userInput.trim().uppercase().toBooleanStrictOrNull() ?: throw BadConfigValueException()
		val value = ColorOptions.from(parsedValue)
		val config = Configuration.load()
		config.color = value
		config.save()
	}
}