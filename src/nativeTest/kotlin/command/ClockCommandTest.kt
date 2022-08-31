package command

import kotlinx.datetime.LocalTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ClockCommandTest {
	var clockInCommand = ClockInCommand()
	var clockOutCommand = ClockOutCommand()
	
	@BeforeTest fun setup() {
		clockInCommand = ClockInCommand()
		clockOutCommand = ClockOutCommand()
	}
	
	@Test fun testParseTimeString() {
		assertEquals(LocalTime(17, 31), clockInCommand.parseTimeString("17:31"))
		assertEquals(LocalTime(17, 32), clockInCommand.parseTimeString("17:32 PM"))
		assertEquals(LocalTime(17, 33), clockInCommand.parseTimeString("5:33 PM"))
		assertEquals(LocalTime(0, 30), clockInCommand.parseTimeString("12:30 AM"))
		assertEquals(LocalTime(12, 30), clockInCommand.parseTimeString("12:30 PM"))
		assertEquals(LocalTime(6, 12), clockInCommand.parseTimeString("6:12AM"))
		assertEquals(LocalTime(6, 14), clockInCommand.parseTimeString("6:14am"))
	}
}