group = "com.github.stephenhamiltonc"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") version "1.8.21" apply false
    kotlin("android") version "1.8.21" apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") version "1.4.0" apply false
}