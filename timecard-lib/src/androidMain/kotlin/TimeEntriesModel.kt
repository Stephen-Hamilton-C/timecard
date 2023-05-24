import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

class TimeEntriesModel : ViewModel(), ITimeEntries {
    var entries by mutableStateOf(listOf<TimeEntry>())
        private set

    override val isClockedIn: Boolean
        get() = entries.lastOrNull()?.end == null
    override val isClockedOut: Boolean
        get() = !isClockedIn

    private val timeEntries = TimeEntries()

    override fun load(data: String) {
        timeEntries.load(data)
        entries = timeEntries.entries
    }

    override fun toString(): String {
        return timeEntries.toString()
    }

    override fun filterByDate(date: LocalDate): List<TimeEntry> {
        return timeEntries.filterByDate(date)
    }

    override fun clockIn(time: Instant): ClockResult {
        val result = timeEntries.clockIn(time)
        entries = timeEntries.entries
        return result
    }

    override fun clockOut(time: Instant): ClockResult {
        val result = timeEntries.clockOut(time)
        entries = timeEntries.entries
        return result
    }

    override fun undo(): UndoResult {
        val result = timeEntries.undo()
        entries = timeEntries.entries
        return result
    }

    override fun calculateMinutesWorked(date: LocalDate): Long {
        return timeEntries.calculateMinutesWorked(date)
    }

    override fun calculateMinutesOnBreak(date: LocalDate): Long {
        return timeEntries.calculateMinutesOnBreak(date)
    }
}