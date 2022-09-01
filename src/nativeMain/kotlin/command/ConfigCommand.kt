package command

import BadConfigValueException
import Color.MAGENTA
import Color.RESET
import Color.magenta
import Color.yellow
import config.ConfigList
import config.IConfig
import kotlin.system.exitProcess

class ConfigCommand : ICommand {
	override val name = "CONFIG"
	override val args: List<String>
		get() = TODO("Not yet implemented")
	override val description: String
		get() = TODO("Not yet implemented")
	override val shortDescription: String
		get() = TODO("Not yet implemented")
	
	private fun listConfigs() {
		println("Config name - Possible values:")
		ConfigList.configs.forEach {
			println("  ${listConfig(it)}")
		}
	}
	
	private fun listConfig(config: IConfig) {
		println("${magenta(config.name)} - $MAGENTA${config.possibleValues.joinToString("$RESET, $MAGENTA")}$RESET")
	}
	
	override fun execute(args: List<String>) {
		if(args.size < 2) {
			// No config selected, list all possible configs
			listConfigs()
		} else {
			// User supplied config name, try to find it
			val foundConfig = ConfigList.configs.find {
				it.name == args[1].lowercase()
			}
			if(foundConfig == null) {
				// No config found, cannot continue
				println(yellow("Unknown config. Use 'timecard config' for a list of configs."))
				exitProcess(1)
			}
			
			// Config found, see if user provided value
			val inputValue = args.getOrNull(2)
			if(inputValue == null) {
				// No value given, show current value and list possible values
				println("${magenta(foundConfig.name)} is currently set to ${magenta(foundConfig.value)}")
				listConfig(foundConfig)
			} else {
				try {
					// Set value. IConfig should automatically parse the input string to a config value
					foundConfig.value = inputValue
				} catch (_: BadConfigValueException) {
					// User gave bad value, show them possible values
					println(yellow("Unknown value for ${magenta(foundConfig.name)}.\n" +
							"Use 'timecard config ${foundConfig.name}' for list of possible values."))
					exitProcess(1)
				}
			}
		}
	}
	
}