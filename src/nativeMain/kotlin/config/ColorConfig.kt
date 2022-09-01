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
	override val possibleValues: List<String> = ColorOptions.values().map { it.name.uppercase() }
	override var value: String = ""
		get() {
			if(field.isEmpty()) {
				field = Configuration.load().color.toString()
			}
			return field
		}
		set(userInput) {
			val parsedValue = userInput.trim().uppercase().toBooleanStrictOrNull() ?: throw BadConfigValueException()
			val value = ColorOptions.from(parsedValue)
			val config = Configuration.load()
			config.color = value
			config.save()
		}
}