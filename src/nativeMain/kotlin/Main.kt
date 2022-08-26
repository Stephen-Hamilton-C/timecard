import kotlinx.coroutines.runBlocking
import persistence.Configuration

fun main() = runBlocking<Unit> {
    val config = Configuration.load()
    config.test++
    config.save()
    println(config.test)
}
