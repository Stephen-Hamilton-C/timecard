# Build from Source

## Dependencies
- Download and install [JDK 11 or higher](https://www.oracle.com/java/technologies/downloads/#java17)

## Any Platform
1. Run `./gradlew linkReleaseExecutableNative`, or `.\gradlew.bat linkReleaseExecutableNative` on Windows 2. 
2. Find the `.kexe` or `.exe` file in `./build/bin/native/releaseExecutable`

## Linux Snap
1. Run `sudo snap install snapcraft --classic`
2. Run `sudo snap install multipass`
3. Run `./gradlew buildSnap`
4. Run `snap install ./timecard_*.snap --devmode`
5. Run `timecard help` to get started
