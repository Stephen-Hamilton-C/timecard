package core

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
import kotlin.test.assertTrue

class TimeEntriesTest {
	val timecardFile = localVfs(AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C"))["timecard_${Util.TODAY}.json"]
	val origTimecardFile = localVfs(AppDirs.dataUserDir("timecard", "Stephen-Hamilton-C"))["timecard_${Util.TODAY}.json.orig"]
	val time1 = LocalTime(6, 12)
	val time2 = LocalTime(8, 36)
	val time3 = LocalTime(17, 31)
	val time4 = LocalTime(17, 32)
	val emptyEntriesData = "{}"
	val oneEntryInData = "{\"entries\":[{\"startTime\":\"06:12\"}]}"
	val oneEntryOutData = "{\"entries\":[{\"startTime\":\"06:12\",\"endTime\":\"08:36\"}]}"
	val twoEntriesInData = "{\"entries\":[{\"startTime\":\"06:12\",\"endTime\":\"08:36\"},{\"startTime\":\"17:31\"}]}"
	val twoEntriesOutData = "{\"entries\":[{\"startTime\":\"06:12\",\"endTime\":\"08:36\"},{\"startTime\":\"17:31\",\"endTime\":\"17:32\"}]}"
	
	var emptyEntries: TimeEntries? = null
	var oneEntryIn: TimeEntries? = null
	var oneEntryOut: TimeEntries? = null
	var twoEntriesIn: TimeEntries? = null
	var twoEntriesOut: TimeEntries? = null
	
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
	
	@Test fun failsOnBadConstruction() {
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
		runBlocking {
			testSaveForTimeEntries(emptyEntries!!, emptyEntriesData)
			testSaveForTimeEntries(oneEntryIn!!, oneEntryInData)
			testSaveForTimeEntries(oneEntryOut!!, oneEntryOutData)
			testSaveForTimeEntries(twoEntriesIn!!, twoEntriesInData)
			testSaveForTimeEntries(twoEntriesOut!!, twoEntriesOutData)
		}
	}
	
	private suspend fun testLoadForTimeEntries(timeEntries: TimeEntries, timeEntriesData: String) {
		timecardFile.writeString(timeEntriesData)
		val loadedTimeEntries = TimeEntries.load()
		assertEquals(timeEntries, loadedTimeEntries)
	}
	@Test fun testLoad() {
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
		TODO()
	}
	
	@Test fun testClockOut() {
		TODO()
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
		
		assertFailsWith<IllegalStateException> {
			emptyEntries!!.undo()
		}
	}
	
}