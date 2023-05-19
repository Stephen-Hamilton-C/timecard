import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.jvm.JvmStatic
import kotlin.time.Duration

class TimeEntry(val start: Instant, val end: Instant? = null) {
    companion object {
        @JvmStatic
        fun from(data: String): TimeEntry {
            val dataSplit = data.split(",")
            val start = Instant.fromEpochSeconds(dataSplit[0].toLong())
            val end = if(dataSplit.size > 1) {
                Instant.fromEpochSeconds(dataSplit[1].toLong())
            } else null

            return TimeEntry(start, end)
        }
    }

    override fun toString(): String {
        return if(end == null) {
            start.epochSeconds.toString()
        } else {
            "${start.epochSeconds},${end.epochSeconds}"
        }
    }

    fun timeBetween(): Duration {
        val end = end ?: Clock.System.now()
        return end - start
    }
}