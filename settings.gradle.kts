rootProject.name = "FallingTree"

enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "MinecraftForge"
            url = uri("https://maven.minecraftforge.net")
        }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "net.minecraftforge.gradle") {
                useModule("${requested.id}:ForgeGradle:${requested.version}")
            }
        }
    }
}

val includeFabric: String by settings
val includeForge: String by settings

include("common")
if (includeFabric.toBoolean()) {
    include("fabric")
}
if (includeForge.toBoolean()) {
    include("forge")
}
