import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

interface ITimeEntries {
    val isClockedIn: Boolean
    val isClockedOut: Boolean

    fun load(data: String)
    override fun toString(): String

    fun filterByDay(date: LocalDate = LocalDate.today()): List<TimeEntry>
    fun filterByDateRange(fromDate: LocalDate, toDate: LocalDate = LocalDate.today()): List<TimeEntry>
    fun clean(pastDate: LocalDate = LocalDate.today()): CleanResult
    fun clockIn(time: Instant = Clock.System.now()): ClockResult
    fun clockOut(time: Instant = Clock.System.now()): ClockResult
    fun undo(): UndoResult
    fun calculateMinutesWorked(date: LocalDate = LocalDate.today()): Long
    fun calculateMinutesOnBreak(date: LocalDate = LocalDate.today()): Long
}