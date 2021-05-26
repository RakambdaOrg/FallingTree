plugins {
    id("fabric-loom") version ("0.8-SNAPSHOT")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(minecraft.officialMojangMappings())
    modImplementation(libs.bundles.fabric)
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
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

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
