package config

import BadConfigValueException

enum class TimeFormatOptions() {
	WRITTEN,
	WRITTEN_SHORT,
	ISO,
	QUARTER_HOUR,
}

class TimeFormatConfig : IConfig {
	override val name = "time_format"
	override val description = "Determines how time is formatted in the Status command"
	override val possibleValues: List<String> = TimeFormatOptions.values().map { it.name.uppercase() }
	override fun retrieveValue(): String = Configuration.load().timeFormat.toString()
	override fun setValue(userInput: String) {
		try {
			val value = TimeFormatOptions.valueOf(userInput.uppercase())
			val config = Configuration.load()
			config.timeFormat = value
			config.save()
		} catch(iae: IllegalArgumentException) {
			throw BadConfigValueException()
		}
	}
}