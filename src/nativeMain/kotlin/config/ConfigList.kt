package config

object ConfigList {
	val configs: List<IConfig> = listOf(
		ColorConfig(),
		TimeFormatConfig(),
	)
}