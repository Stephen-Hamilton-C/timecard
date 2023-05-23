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
        _entries.clear()

        val entriesData = data.split(";")
        for(entryData in entriesData) {
            val entry = TimeEntry.from(entryData)
            _entries.add(entry)
        }
    }
    override fun toString(): String = _entries.joinToString(";")

    override fun filterByDate(date: LocalDate): List<TimeEntry> {
        TODO("Implement filterByDate")
    }

    private fun timeIsFuture(time: Instant): Boolean {
        val now = Clock.System.now()
        return now < time
    }

    override fun clockIn(time: Instant): ClockResult {
        if(isClockedIn) return ClockResult.NO_OP
        if(timeIsFuture(time)) return ClockResult.TIME_IN_FUTURE
        
        val lastEntry = entries.lastOrNull()
        if(lastEntry != null && lastEntry.end >= time)
            return ClockResult.TIME_TOO_EARLY

        val newEntry = TimeEntry(time)
        _entries.add(newEntry)

        return ClockResult.SUCCESS
    }

    override fun clockOut(time: Instant): ClockResult {
        if(isClockedOut) return ClockResult.NO_OP
        if(timeIsFuture(time)) return ClockResult.TIME_IN_FUTURE

        val lastEntry = entries.last()
        if(lastEntry.start >= time)
            return ClockResult.TIME_TOO_EARLY

        val newEntry = TimeEntry(lastEntry.start, time)
        _entries.removeLast()
        _entries.add(newEntry)

        return ClockResult.SUCCESS
    }

    override fun undo() {
        if(_entries.isEmpty()) return UndoResult.NO_OP

        if(isClockedIn) {
            _entries.removeLast()
        } else {
            val lastEntry = _entries.removeLast()
            val newEntry = TimeEntry(lastEntry.start)
            _entries.add(newEntry)
        }

        return UndoResult.SUCCESS
    }

    override fun calculateMinutesWorked(date: LocalDate): Long {
        var totalMinutes = 0L
        for(entry in _entries) {
            val endTime = entry.end ?: Clock.System.now()
            val duration = endTime - entry.start
            totalMinutes += duration.inWholeMinutes()
        }

        return totalMinutes
    }

    override fun calculateMinutesOnBreak(date: LocalDate): Long {
        TODO("Implement calculateMinutesOnBreak")
    }
}
