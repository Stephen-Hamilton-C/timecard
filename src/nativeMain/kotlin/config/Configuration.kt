package config

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import core.ClassFileIO
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
	var color: BooleanOptions = BooleanOptions.from(Platform.osFamily != OsFamily.WINDOWS),
	var hour24: BooleanOptions = BooleanOptions.FALSE,
	var timeFormat: TimeFormatOptions = TimeFormatOptions.WRITTEN,
	var cleanInterval: CleanIntervalOptions = CleanIntervalOptions.WEEKLY,
) {
	companion object {
		val configDir = AppDirs.configUserDir("timecard", "Stephen-Hamilton-C")
		
		fun load(): Configuration = runBlocking {
			// Unfortunately, I can't make a var in an object in Kotlin Native...
			// but this is going to be costly if this isn't cached.
			// May have to make an ugly workaround for this.
			// Or maybe not. It is Native, which should be fast.
			// If it needs optimization, I can do it later.
			val manager = ClassFileIO(localVfs(configDir)["config.json"])
			return@runBlocking manager.load(::Configuration)
		}
	}
	
	fun save() = runBlocking {
		val manager = ClassFileIO(localVfs(configDir)["config.json"])
		manager.save(this@Configuration)
	}
}