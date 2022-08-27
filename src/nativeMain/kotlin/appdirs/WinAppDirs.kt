package appdirs

import kotlinx.cinterop.toKString
import platform.posix.getenv

object WinAppDirs : IAppDirs {
    
    val _appData = getenv("LOCALAPPDATA")?.toKString() ?: throw Exception("LocalAppData environment variable does not exist!")
    
    override fun configUserDir(name: String, author: String, version: String): String {
        return "$_appData\\$author\\$name\\$version"
    }
    
    override fun configUserDir(name: String, author: String): String {
        return "$_appData\\$author\\$name"
    }
    
    override fun configUserDir(name: String): String {
        return "$_appData\\$name"
    }
    
    override fun dataUserDir(name: String, author: String, version: String): String {
        return "$_appData\\$author\\$name\\$version"
    }
    
    override fun dataUserDir(name: String, author: String): String {
        return "$_appData\\$author\\$name"
    }
    
    override fun dataUserDir(name: String): String {
        return "$_appData\\$name"
    }
    
}