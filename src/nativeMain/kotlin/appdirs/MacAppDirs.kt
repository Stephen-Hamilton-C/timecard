package appdirs

import kotlinx.cinterop.toKString
import platform.posix.getenv

object MacAppDirs : IAppDirs {
    
    private val _home = getenv("HOME")?.toKString() ?: "~"
    private val _pref = "$_home/Library/Preferences"
    private val _appSupport = "$_home/Library/Application Support"
    
    override fun configUserDir(name: String, author: String, version: String): String {
        return "$_pref/$author/$name/$version"
    }
    
    override fun configUserDir(name: String, author: String): String {
        return "$_pref/$author/$name"
    }
    
    override fun configUserDir(name: String): String {
        return "$_pref/$name"
    }

    override fun dataUserDir(name: String, author: String, version: String): String {
        return "$_appSupport/$author/$name/$version"
    }
    
    override fun dataUserDir(name: String, author: String): String {
        return "$_appSupport/$author/$name"
    }
    
    override fun dataUserDir(name: String): String {
        return "$_appSupport/$name"
    }
}