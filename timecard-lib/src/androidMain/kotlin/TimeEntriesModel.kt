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

    private val _timeEntries = TimeEntries()

    override fun load(data: String) {
        _timeEntries.load(data)
        entries = _timeEntries.entries
    }

    override fun toString(): String {
        return _timeEntries.toString()
    }

    override fun filterByDay(date: LocalDate): List<TimeEntry> {
        return _timeEntries.filterByDay(date)
    }

    override fun filterByDateRange(fromDate: LocalDate, toDate: LocalDate): List<TimeEntry> {
        return _timeEntries.filterByDateRange(fromDate, toDate)
    }

    override fun clean(pastDate: LocalDate): CleanResult {
        return _timeEntries.clean(pastDate)
    }

    override fun clockIn(time: Instant): ClockResult {
        val result = _timeEntries.clockIn(time)
        entries = _timeEntries.entries
        return result
    }

    override fun clockOut(time: Instant): ClockResult {
        val result = _timeEntries.clockOut(time)
        entries = _timeEntries.entries
        return result
    }

    override fun undo(): UndoResult {
        val result = _timeEntries.undo()
        entries = _timeEntries.entries
        return result
    }

    override fun calculateMinutesWorked(date: LocalDate): Long {
        return _timeEntries.calculateMinutesWorked(date)
    }

    override fun calculateMinutesOnBreak(date: LocalDate): Long {
        return _timeEntries.calculateMinutesOnBreak(date)
    }
}