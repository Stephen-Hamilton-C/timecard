package core

import config.Configuration

@Suppress("unused")
object Color {

	private val colorEnabled = Configuration.load().color.value

	// core.Color code strings from
	// http://www.topmudsites.com/forums/mud-coding/413-java-ansi.html
	val BLACK: String = if(colorEnabled) { "\u001B[30m" } else { "" }
	val RED: String = if(colorEnabled) { "\u001B[31m" } else { "" }
	val GREEN: String = if(colorEnabled) { "\u001B[32m" } else { "" }
	val YELLOW: String = if(colorEnabled) { "\u001B[33m" } else { "" }
	val BLUE: String = if(colorEnabled) { "\u001B[34m" } else { "" }
	val MAGENTA: String = if(colorEnabled) { "\u001B[35m" } else { "" }
	val CYAN: String = if(colorEnabled) { "\u001B[36m" } else { "" }
	val WHITE: String = if(colorEnabled) { "\u001B[37m" } else { "" }
	val RESET: String = if(colorEnabled) { "\u001B[0m" } else { "" }

	fun black(string: Any?) = colorize(BLACK, string)
	fun red(string: Any?) = colorize(RED, string)
	fun green(string: Any?) = colorize(GREEN, string)
	fun yellow(string: Any?) = colorize(YELLOW, string)
	fun blue(string: Any?) = colorize(BLUE, string)
	fun magenta(string: Any?) = colorize(MAGENTA, string)
	fun cyan(string: Any?) = colorize(CYAN, string)
	fun white(string: Any?) = colorize(WHITE, string)

	private fun colorize(color: String, string: Any?) = "$color${string.toString()}$RESET"

}