package config

import BadConfigValueException

enum class CleanIntervalOptions(val days: Int) {
	MANUALLY(-1),
	DAILY(1),
	WEEKLY(7),
	MONTHLY(30),
}

class CleanIntervalConfig : IConfig {
	override val name = "clean_interval"
	override val description = "How long to keep old timecards."
	override val possibleValues: List<String> = CleanIntervalOptions.values().map { it.name.uppercase() }
	
	override fun retrieveValue(): String = Configuration.load().cleanInterval.toString()
	
	override fun setValue(userInput: String) {
		try {
			val value = CleanIntervalOptions.valueOf(userInput.uppercase())
			val config = Configuration.load()
			config.cleanInterval = value
			config.save()
		} catch(iae: IllegalArgumentException) {
			throw BadConfigValueException()
		}
	}
	
}