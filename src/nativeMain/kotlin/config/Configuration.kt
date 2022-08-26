package config

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import core.ClassFileManager
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    var test: Int = 0,
) {
    companion object {
        private val _configDir = AppDirs.configUserDir("timecard", "Stephen-Hamilton-C")

        suspend fun load(): Configuration {
            val manager = ClassFileManager(localVfs(_configDir)["config.json"])
            return manager.load(::Configuration)
        }
    }

    suspend fun save() {
        val manager = ClassFileManager(localVfs(_configDir)["config.json"])
        manager.save(this)
    }
}