import kotlinx.datetime.LocalDate
import kotlinx.datetime.Instant

class TimeEntries() : ITimeEntries {
    val entries: List<TimeEntry>
        get() = _entries
    private val _entries = mutableListOf<TimeEntry>()

    override val isClockedIn: Boolean
        get() = entries.lastOrNull()?.end == null
    override val isClockedOut: Boolean
        get() = !isClockedIn

    override fun load(data: String) {
        TODO("Implement load")
    }
    override fun toString(): String {
        TODO("Implement toString")
    }

    override fun filterByDate(date: LocalDate): List<TimeEntry> {
        TODO("Implement filterByDate")
    }
    override fun clockIn(time: Instant) {
        TODO("Implement clockIn")
    }
    override fun clockOut(time: Instant) {
        TODO("Implement clockOut")
    }
    override fun undo() {
        TODO("Implement undo")
    }
    override fun calculateMinutesWorked(date: LocalDate): Long {
        TODO("Implement calculateMinutesWorked")
    }
    override fun calculateMinutesOnBreak(date: LocalDate): Long {
        TODO("Implement calculateMinutesOnBreak")
    }
}
