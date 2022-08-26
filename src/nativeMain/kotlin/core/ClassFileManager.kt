package core

import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.fullName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// TODO: Need better name
class ClassFileManager(val file: VfsFile) {
	suspend inline fun <reified T> save(obj: T) {
		file.ensureParents()
		file.writeString(Json.encodeToString(obj))
	}
	
	suspend inline fun <reified T> load(defaultConstructor: () -> T): T {
		if (!this.file.exists()) return defaultConstructor()
		val data = this.file.readString()
		return try {
			Json.decodeFromString(data)
		} catch (e: Exception) {
			val parentDir = this.file.parent
			val fileName = this.file.fullName
			this.file.renameTo(parentDir["$fileName.old"].absolutePath)
			println("Error while reading $fileName! It has been reset. Original file now has the .old postfix if you wish to manually recover the file.")
			defaultConstructor()
		}
	}
}