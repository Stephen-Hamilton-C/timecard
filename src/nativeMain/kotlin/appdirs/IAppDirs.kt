package appdirs

interface IAppDirs {
    fun configUserDir(name: String, author: String, version: String = ""): String
    fun configUserDir(name: String, author: String): String
    fun configUserDir(name: String): String
    
    fun dataUserDir(name: String, author: String, version: String = ""): String
    fun dataUserDir(name: String, author: String): String
    fun dataUserDir(name: String): String
}