package core

import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.fullName
import core.Color.yellow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Standardizes how classes get loaded and saved to files.
 * Create a new ClassFileIO with a VfsFile to where you expect the data for the class to be saved.
 */
class ClassFileIO(val file: VfsFile) {
	/**
	 * Saves the object to the file specified in the constructor.
	 * @param obj An object that is a Serializable class.
	 */
	suspend inline fun <reified T> save(obj: T) {
		// Create parent directories, then serialize to Json and save to file.
		// VfsFile.ensureParents() is a thing that exists, but it seems to not work across different OSes...
		Util.mkdirRecursive(file.parent)
		file.writeString(Json.encodeToString(obj))
	}
	
	/**
	 * Creates an object from serialized data. Object must be a Serializable class.
	 * @param defaultConstructor The default constructor for the object you wish to create.
	 * Must either have no or all optional arguments.
	 * Denoted with `::ClassName`.
	 * This is used if there are problems reading or deserializing the file.
	 */
	suspend inline fun <reified T> load(defaultConstructor: () -> T): T {
		// You know, this is one of very few times I'm happy functions are objects
		if (!this.file.exists()) return defaultConstructor()
		return try {
			val data = this.file.readString()
			Json.decodeFromString(data)
		} catch (e: Exception) {
			val parentDir = this.file.parent
			val fileName = this.file.fullName
			this.file.renameTo(parentDir["$fileName.old"].absolutePath)
			println(yellow("Error while reading $fileName! It has been reset. Original file now has the .old postfix if you wish to manually recover the file."))
			defaultConstructor()
		}
	}
}