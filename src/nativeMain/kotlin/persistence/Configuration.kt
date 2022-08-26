package persistence

import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Configuration(
    var test: Int = 0,
) {
    companion object {
        private val CONFIG_DIR = localVfs(AppDirs.configUserDir("timecard", "Stephen-Hamilton-C"))
        private val FILE = CONFIG_DIR["config.json"]

        suspend fun load(): Configuration {
            if (!FILE.exists()) return Configuration()
            val configData = FILE.readString()
            return try {
                Json.decodeFromString(configData)
            } catch(e: Exception) {
                FILE.renameTo(CONFIG_DIR["config.json.old"].absolutePath)
                println("Error while retrieving config! Config has been reset, original file now has the .old postfix if you wish to manually recover the file.")
                Configuration()
            }
        }
    }

    suspend fun save() {
        FILE.ensureParents()
        FILE.writeString(Json.encodeToString(this))
    }
}