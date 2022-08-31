class InvalidTimeException(val afterNow: Boolean) : Exception() {}
class ClockedInException : Exception() {}
class ClockedOutException : Exception() {}
