enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "FallingTree"
include("common")
include("fabric")
//TODO: Renable for 1.18
//include("forge")
