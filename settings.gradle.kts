enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        jcenter()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }

    val loom_version: String by settings
    plugins {
        id("fabric-loom").version(loom_version)
    }
}
rootProject.name = "FallingTree"
include("common")
include("fabric")
//include("forge")
