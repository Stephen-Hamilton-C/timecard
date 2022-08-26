package appdirs

object UnixAppDirs : IAppDirs {
    override fun configUserDir(name: String, author: String, version: String): String {
//        TODO("Not yet implemented")
        return "/home/stephen/.config/$name"
    }

    override fun dataUserDir(name: String, author: String, version: String): String {
//        TODO("Not yet implemented")
        return "/home/stephen/.local/share/$name"
    }

}