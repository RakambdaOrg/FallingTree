plugins {
    id("fabric-loom") version ("0.8-SNAPSHOT")
}

dependencies {
    val minecraft_version: String by project
    val loader_version: String by project
    val fabric_version: String by project

    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(minecraft.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")
    implementation(project(":common"))

    modImplementation(libs.modmenu) {
        exclude(group = "net.fabricmc.fabric-api")
    }

    modImplementation(libs.clothConfigFabric) {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(module = "modmenu")
    }
    include(libs.clothConfigFabric)
}

tasks {
    processResources {
//        duplicatesStrategy(DuplicatesStrategy.INCLUDE)
        from("src/main/resources", "../common/src/main/resources")

        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.isDeprecation = true
        options.release.set(16)
    }
}

loom {
    val modId: String by project
    refmapName = "fabric.${modId}.refmap.json"

    runs {
        create("fallingTreeClient") {
            client()

            property("fabric.log.level", "debug")
            vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
        }
        create("fallingTreeServer") {
            server()

            property("fabric.log.level", "debug")
            vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
        }
    }
}
