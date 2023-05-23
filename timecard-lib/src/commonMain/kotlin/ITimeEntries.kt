import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

interface ITimeEntries {
    val isClockedIn: Boolean
    val isClockedOut: Boolean

    fun load(data: String)
    override fun toString(): String

    fun filterByDate(date: LocalDate = Util.today()): List<TimeEntry>
    fun clockIn(time: Instant = Clock.System.now()): ClockResult
    fun clockOut(time: Instant = Clock.System.now()): ClockResult
    fun undo()
    fun calculateMinutesWorked(date: LocalDate = Util.today()): Long
    fun calculateMinutesOnBreak(date: LocalDate = Util.today()): Long
}