plugins {
    alias(libs.plugins.loom)
}

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.bundles.fabric) {
        exclude(module = "fabric-api-deprecated")
    }

    implementation(project(":common"))

    api(libs.modmenu) {
        exclude(group = "net.fabricmc.fabric-api")
    }

    implementation(libs.clothConfigFabric) {
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
}

loom {
    val modId: String by project

    splitEnvironmentSourceSets()

    mods {
        register("fallingtree") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }

    mixin {
        defaultRefmapName.set("fabric.${modId}.refmap.json")
    }

    runs {
        create("FTFabricClient") {
            client()
            runDir("run/client")

            property("fabric.log.level", "info")
            vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
            programArgs("--uuid=f13e3278-dfb8-4948-98cc-7701b5c62e8c", "--username=Dev")
        }
        create("FTFabricServer") {
            server()
            runDir("run/server")

            property("fabric.log.level", "info")
            vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
        }
    }
}

fabricApi {
    configureTests {
        createSourceSet = true
        modId = "fallingtree_gametest"
        enableClientGameTests = false
    }
}

sourceSets.named("gametest") {
    // Loader independent scenarios, shared with Forge and NeoForge
    java.srcDir("../gametest/src/main/java")
    resources.srcDir("../gametest/src/main/resources")
}

// Game tests are run on every loader through the root "gameTest" task rather than as part of "check"
tasks.named("check") {
    setDependsOn(dependsOn.filterNot { it == "runGameTest" })
}

loom.runs.named("gameTest") {
    property("fabric-api.gametest.report-file", layout.buildDirectory.file("gametest/junit.xml").get().asFile.absolutePath)
    vmArg("-XX:+ShowCodeDetailsInExceptionMessages")
}
