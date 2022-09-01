package config

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import core.ClassFileManager
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

// TODO: See about making this an Object
@Serializable
data class Configuration(
	var color: ColorOptions = ColorOptions.from(Platform.osFamily != OsFamily.WINDOWS)
) {
	companion object {
		private val _configDir = AppDirs.configUserDir("timecard", "Stephen-Hamilton-C")
		
		fun load(): Configuration = runBlocking {
			// TODO: Unfortunately, I can't make a var in an object in Kotlin Native...
			// but this is going to be costly if this isn't cached.
			// May have to make an ugly workaround for this.
			// Or maybe not. It is Native, which should be fast.
			// If it needs optimization, I can do it later.
			val manager = ClassFileManager(localVfs(_configDir)["config.json"])
			return@runBlocking manager.load(::Configuration)
		}
	}
	
	fun save() = runBlocking {
		val manager = ClassFileManager(localVfs(_configDir)["config.json"])
		manager.save(this@Configuration)
	}
}