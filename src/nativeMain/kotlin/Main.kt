import config.Configuration

fun main() {
    val config = Configuration.load()
    config.test++
    config.save()
    println(config.test)
}
