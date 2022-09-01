package config

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import core.ClassFileManager
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    var color: ColorOptions = ColorOptions.from(Platform.osFamily != OsFamily.WINDOWS)
) {
    companion object {
        private val _configDir = AppDirs.configUserDir("timecard", "Stephen-Hamilton-C")

        fun load() = runBlocking {
            val manager = ClassFileManager(localVfs(_configDir)["config.json"])
            return@runBlocking manager.load(::Configuration)
        }
    }

    fun save() = runBlocking {
        val manager = ClassFileManager(localVfs(_configDir)["config.json"])
        manager.save(this@Configuration)
    }
}