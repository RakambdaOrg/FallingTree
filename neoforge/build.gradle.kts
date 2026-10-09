plugins {
    id("java-library")
    alias(libs.plugins.neoforge)
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
    create("gametest") {
        // Loader independent scenarios, shared with Fabric and Forge
        java.srcDir("../gametest/src/main/java")
        resources.srcDir("../gametest/src/main/resources")
        compileClasspath += main.get().output
        runtimeClasspath += main.get().output
    }
}

configurations.named("gametestImplementation") {
    extendsFrom(configurations.implementation.get())
}

neoForge {
    val modId: String by project

    version = libs.versions.neoforgeVersion.get()

    addModdingDependenciesTo(sourceSets["gametest"])

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
        create("fallingtree_gametest") {
            sourceSet(sourceSets["gametest"])
        }
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "error")
            logLevel = org.slf4j.event.Level.DEBUG
        }

        register("client") {
            client()
            ideName = "runFTNeoForgeClient"
            gameDirectory = project.file("./run/client")
            loadedMods = setOf(mods[modId])
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("server") {
            server()
            ideName = "runFTNeoForgeServer"
            gameDirectory = project.file("./run/server")
            programArgument("--nogui")
            loadedMods = setOf(mods[modId])
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("gameTestServer") {
            type = "gameTestServer"
            ideName = "runFTNeoForgeTestServer"
            gameDirectory = project.file("./run/test")
            sourceSet = sourceSets["gametest"]
            loadedMods = setOf(mods[modId], mods["fallingtree_gametest"])
        }
    }
}

val localRuntime: Configuration by configurations.creating
configurations.runtimeClasspath {
    extendsFrom(localRuntime)
}

dependencies {
    implementation(project(":common"))

    compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${libs.versions.clothConfigVersion.get()}")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from("src/main/resources", "../common/src/main/resources")

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties + mapOf("minecraftVersion" to libs.versions.minecraftVersion.get()))
        }
    }
}
