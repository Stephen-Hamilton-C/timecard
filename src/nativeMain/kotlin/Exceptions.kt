class BadConfigValueException : IllegalArgumentException() {}
class InvalidTimeException(val afterNow: Boolean) : IllegalArgumentException() {}
class ClockedInException : IllegalStateException() {}
class ClockedOutException : IllegalStateException() {}
