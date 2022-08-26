package persistence

import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.localVfs
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Configuration(
    var test: Boolean = true,
) {
    companion object {
        private val PATH = "/home/stephen/.config/timecard/config.json"

        suspend fun load(): Configuration {
            val configFile = localVfs(PATH)
            if (!configFile.exists()){
                return Configuration()
            }
            val configData = configFile.readString()
            return Json.decodeFromString(configData)
        }
    }

    suspend fun save() {
        val configFile = localVfs(PATH)
        configFile.ensureParents()
        configFile.writeString(Json.encodeToString(this))
    }
}