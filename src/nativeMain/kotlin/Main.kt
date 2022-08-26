import kotlinx.coroutines.runBlocking
import config.Configuration

fun main() = runBlocking<Unit> {
    val config = Configuration.load()
    config.test++
    config.save()
    println(config.test)
}
