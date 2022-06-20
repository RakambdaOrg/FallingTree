plugins {
    alias(libs.plugins.loom)
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.bundles.fabric)

    implementation(project(":common"))

    modImplementation(libs.modmenu) {
        exclude(group = "net.fabricmc.fabric-api")
    }

    modImplementation(libs.clothConfigFabric) {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(module = "modmenu")
    }
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
        options.release.set(17)
    }
}

loom {
    val modId: String by project

    splitEnvironmentSourceSets()

    mods {
        create("fallingtree") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }

    mixin {
        defaultRefmapName.set("fabric.${modId}.refmap.json")
    }

    runs {
        create("runFTFabricClient") {
            client()
            runDir(project.file("run").canonicalPath)

            property("fabric.log.level", "debug")
            vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
            programArgs("--uuid=123")
        }
        create("runFTFabricServer") {
            server()
            runDir(project.file("run").canonicalPath)

            property("fabric.log.level", "info")
            vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
        }
    }
}
