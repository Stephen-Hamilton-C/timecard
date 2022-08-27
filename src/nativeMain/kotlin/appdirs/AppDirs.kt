package appdirs

object AppDirs : IAppDirs {
    private val _appdirs: IAppDirs = when(Platform.osFamily) {
        OsFamily.LINUX -> UnixAppDirs
        OsFamily.MACOSX -> MacAppDirs
        OsFamily.WINDOWS -> WinAppDirs
        else -> throw IllegalStateException("Unsupported OS. Expected Windows, macOS, or Linux, but instead found ${Platform.osFamily}")
    }

    override fun configUserDir(name: String, author: String, version: String): String {
        return _appdirs.configUserDir(name, author, version)
    }
    override fun configUserDir(name: String, author: String): String {
        return _appdirs.configUserDir(name, author)
    }
    override fun configUserDir(name: String): String {
        return _appdirs.configUserDir(name)
    }

    override fun dataUserDir(name: String, author: String, version: String): String {
        return _appdirs.dataUserDir(name, author, version)
    }
    override fun dataUserDir(name: String, author: String): String {
        return _appdirs.dataUserDir(name, author)
    }
    override fun dataUserDir(name: String): String {
        return _appdirs.dataUserDir(name)
    }

}