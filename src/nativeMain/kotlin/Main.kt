import com.soywiz.korio.file.std.localCurrentDirVfs
import kotlinx.coroutines.runBlocking
import persistence.Configuration

fun main() = runBlocking<Unit> {
    val config = Configuration.load()
    println(config.test)
    config.test = false
    config.save()
    println(localCurrentDirVfs.absolutePath)
}
