package appdirs

import kotlinx.cinterop.toKString
import platform.posix.getenv

object UnixAppDirs : IAppDirs {
    
    private val _home = getenv("HOME")?.toKString() ?: "~"
    private val _configHome = getenv("XDG_CONFIG_HOME")?.toKString() ?: "$_home/.config"
    private val _dataHome = getenv("XDG_DATA_HOME")?.toKString() ?: "$_home/.local/share"
    
    override fun configUserDir(name: String, author: String, version: String): String {
        return "$_configHome/$name/$version"
    }
    
    override fun configUserDir(name: String, author: String): String {
        return configUserDir(name)
    }
    
    override fun configUserDir(name: String): String {
        return "$_configHome/$name"
    }

    override fun dataUserDir(name: String, author: String, version: String): String {
        return "$_dataHome/$name/$version"
    }
    
    override fun dataUserDir(name: String, author: String): String {
        return dataUserDir(name)
    }
    
    override fun dataUserDir(name: String): String {
        return "$_dataHome/$name"
    }

}