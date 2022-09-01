package core

import ClockedInException
import ClockedOutException
import InvalidTimeException
import appdirs.AppDirs
import com.soywiz.korio.file.std.localVfs
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class TimeEntriesTest {
	private val timecardFile = localVfs(AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C"))["timecard_${Util.TODAY}.json"]
	private val origTimecardFile = localVfs(AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C"))["timecard_${Util.TODAY}.json.orig"]
	private val time1 = LocalTime(6, 12)
	private val time2 = LocalTime(8, 36)
	private val time3 = LocalTime(17, 31)
	private val time4 = LocalTime(17, 32)
	private val emptyEntriesData = "{}"
	private val oneEntryInData = "{\"entries\":[{\"startTime\":\"06:12\"}]}"
	private val oneEntryOutData = "{\"entries\":[{\"startTime\":\"06:12\",\"endTime\":\"08:36\"}]}"
	private val twoEntriesInData = "{\"entries\":[{\"startTime\":\"06:12\",\"endTime\":\"08:36\"},{\"startTime\":\"17:31\"}]}"
	private val twoEntriesOutData = "{\"entries\":[{\"startTime\":\"06:12\",\"endTime\":\"08:36\"},{\"startTime\":\"17:31\",\"endTime\":\"17:32\"}]}"
	
	private var emptyEntries: TimeEntries? = null
	private var oneEntryIn: TimeEntries? = null
	private var oneEntryOut: TimeEntries? = null
	private var twoEntriesIn: TimeEntries? = null
	private var twoEntriesOut: TimeEntries? = null
	
	@BeforeTest fun setup() {
		runBlocking {
			timecardFile.renameTo(origTimecardFile.absolutePath)
		}
		
		emptyEntries = TimeEntries()
		oneEntryIn = TimeEntries(mutableListOf(
			TimeEntry(time1),
		))
		oneEntryOut = TimeEntries(mutableListOf(
			TimeEntry(time1, time2)
		))
		twoEntriesIn = TimeEntries(mutableListOf(
			TimeEntry(time1, time2),
			TimeEntry(time3),
		))
		twoEntriesOut = TimeEntries(mutableListOf(
			TimeEntry(time1, time2),
			TimeEntry(time3, time4),
		))
	}
	
	@AfterTest fun cleanup() {
		runBlocking {
			origTimecardFile.renameTo(timecardFile.absolutePath)
		}
	}
	
	@Test fun testFailOnBadConstruction() {
		assertFailsWith<IllegalStateException> {
			TimeEntries(mutableListOf(
				TimeEntry(time1),
				TimeEntry(time2),
			))
		}
		assertFailsWith<IllegalStateException> {
			TimeEntries(mutableListOf(
				TimeEntry(time1),
				TimeEntry(time2, time3),
			))
		}
		assertFailsWith<IllegalStateException> {
			TimeEntries(mutableListOf(
				TimeEntry(time4, time3),
				TimeEntry(time2)
			))
		}
	}
	
	private suspend fun testSaveForTimeEntries(timeEntries: TimeEntries, timeEntriesData: String) {
		timeEntries.save()
		assertEquals(timeEntriesData, timecardFile.readString())
	}
	@Test fun testSave() {
		// TODO: Figure out how to test this in snaps
		runBlocking {
			testSaveForTimeEntries(emptyEntries!!, emptyEntriesData)
			testSaveForTimeEntries(oneEntryIn!!, oneEntryInData)
			testSaveForTimeEntries(oneEntryOut!!, oneEntryOutData)
			testSaveForTimeEntries(twoEntriesIn!!, twoEntriesInData)
			testSaveForTimeEntries(twoEntriesOut!!, twoEntriesOutData)
		}
	}
	
	@Test fun testFailOnBadEntries() {
		// Test overlapping entries
		twoEntriesIn!!.entries[1].endTime = time1
		assertFailsWith<IllegalStateException> {
			twoEntriesIn!!.save()
		}
		
		// Test null endTime in middle
		twoEntriesOut!!.entries[0].endTime = null
		assertFailsWith<IllegalStateException> {
			twoEntriesOut!!.save()
		}
	}
	
	private suspend fun testLoadForTimeEntries(timeEntries: TimeEntries, timeEntriesData: String) {
		timecardFile.writeString(timeEntriesData)
		val loadedTimeEntries = TimeEntries.load()
		assertEquals(timeEntries, loadedTimeEntries)
	}
	@Test fun testLoad() {
		// TODO: Figure out how to test this in snaps
		runBlocking {
			testLoadForTimeEntries(emptyEntries!!, emptyEntriesData)
			testLoadForTimeEntries(oneEntryIn!!, oneEntryInData)
			testLoadForTimeEntries(oneEntryOut!!, oneEntryOutData)
			testLoadForTimeEntries(twoEntriesIn!!, twoEntriesInData)
			testLoadForTimeEntries(twoEntriesOut!!, twoEntriesOutData)
		}
	}
	
	@Test fun testIsClockedIn() {
		assertTrue(oneEntryIn!!.isClockedIn())
		assertFalse(oneEntryOut!!.isClockedIn())
		assertTrue(twoEntriesIn!!.isClockedIn())
		assertFalse(twoEntriesOut!!.isClockedIn())
	}
	
	@Test fun testIsClockedOut() {
		assertFalse(oneEntryIn!!.isClockedOut())
		assertTrue(oneEntryOut!!.isClockedOut())
		assertFalse(twoEntriesIn!!.isClockedOut())
		assertTrue(twoEntriesOut!!.isClockedOut())
	}
	
	@Test fun testClockIn() {
		emptyEntries!!.clockIn(time2)
		assertEquals(time2, emptyEntries!!.entries[0].startTime)
		
		assertFailsWith<ClockedInException> {
			oneEntryIn!!.clockIn(time2)
		}
		
		oneEntryOut!!.clockIn(time3)
		assertEquals(time3, oneEntryOut!!.entries[1].startTime)
		
		// Test trying to clock in too early
		assertFailsWith<InvalidTimeException> {
			twoEntriesOut!!.clockIn(time1)
		}
	}
	
	@Test fun testClockOut() {
		assertFailsWith<ClockedOutException> {
			emptyEntries!!.clockOut()
		}
		
		oneEntryIn!!.clockOut(time2)
		assertEquals(time2, oneEntryIn!!.entries[0].endTime!!)
		
		assertFailsWith<ClockedOutException> {
			oneEntryOut!!.clockOut(time3)
		}
		
		// Test trying to clock out too early
		assertFailsWith<InvalidTimeException> {
			twoEntriesIn!!.clockOut(time1)
		}
	}
	
	@Test fun testLastStartTime() {
		assertNull(emptyEntries!!.lastStartTime())
		assertSame(oneEntryIn!!.lastStartTime(), oneEntryIn!!.entries[0].startTime)
		assertSame(twoEntriesOut!!.lastStartTime(), twoEntriesOut!!.entries[1].startTime)
	}
	
	@Test fun testLastEndTime() {
		assertNull(emptyEntries!!.lastEndTime())
		assertNull(oneEntryIn!!.lastEndTime())
		assertSame(twoEntriesOut!!.lastEndTime(), twoEntriesOut!!.entries[1].endTime)
	}
	
	@Test fun testUndo() {
		twoEntriesOut!!.undo()
		assertEquals(twoEntriesIn, twoEntriesOut)
		
		twoEntriesIn!!.undo()
		assertEquals(oneEntryOut, twoEntriesIn)
		
		oneEntryOut!!.undo()
		assertEquals(oneEntryIn, oneEntryOut)
		
		oneEntryIn!!.undo()
		assertEquals(emptyEntries, oneEntryIn)
		
		assertNull(emptyEntries!!.undo())
	}
	
}