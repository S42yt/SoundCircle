pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
}

stonecutter {
    create(rootProject) {
        versions("1.21.11", "26.1", "26.2", "26.3")
        vcsVersion = "26.3"
    }
}

rootProject.name = "SoundCircle"
