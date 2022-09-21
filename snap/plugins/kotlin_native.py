import snapcraft
import os
import shutil
import glob


class MyPlugin(snapcraft.BasePlugin):

    def build(self):
        super().build()

        # Snapcraft API is so broken. I can't access any attributes of super().
        # But vars can see them, so here I am, doing this awful hack.
        superVars = vars(super())
        partdir = superVars['partdir']
        installdir = superVars['installdir']

        # Build with Gradle
        print("Building release executable...")
        os.system("./gradlew linkReleaseExecutableNative")
 
        # Get executable and strip its extension 
        # because no one wants to type .kexe after the name of the command
        artifactDir = "./build/bin/native/releaseExecutable"
        executable = glob.glob(artifactDir + "/*.kexe")[0]
        executableName = os.path.splitext(os.path.basename(executable))[0]

        # Place build artifact into part staging area without .kexe extension
        shutil.copy2(executable, os.path.join(installdir, executableName))
