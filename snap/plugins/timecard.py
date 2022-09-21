import snapcraft
import os
import shutil


# Snapcraft doesn't have a built-in plugin for Kotlin Native

# But what about the 'gradle' plugin?
# Yeah if your build doesn't spit out a jar, it throws an error, even if it finds build artifacts.

# Ok, why not just write your own Kotlin Native plugin instead of making this project specific?
# Because the Snapcraft API is absolutely broken. I can see the partdirs variable in super() using vars(),
# But I can't access the variable. It just says "the property does not exist" even though I can literally see it.
class MyPlugin(snapcraft.BasePlugin):

    def build(self):
        super().build()

        print("Building release executable...")
        os.system("./gradlew linkReleaseExecutableNative")
        shutil.copy2("./build/bin/native/releaseExecutable/timecard.kexe", "../prime/timecard")
 
