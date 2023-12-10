rootProject.name = "FallingTree"

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
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.7.0")
}

val includeFabric: String by settings
val includeForge: String by settings
val includeNeoForge: String by settings

include("common")
if (includeFabric.toBoolean()) {
    include("fabric")
}
if (includeForge.toBoolean()) {
    include("forge")
}
if (includeNeoForge.toBoolean()) {
    include("neoforge")
}
