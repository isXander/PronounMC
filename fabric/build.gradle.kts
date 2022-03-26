plugins {
    id("com.github.johnrengelman.shadow") version "7.+"
}

base.archivesName.set("pronounmc-${project.name}")

architectury {
    platformSetupLoomIde()
    fabric()
}

val common by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentFabric"].extendsFrom(this)
}
val shadowCommon by configurations.creating

dependencies {
    val fabricLoaderVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    listOf(
        "fabric-lifecycle-events-v1",
        "fabric-resource-loader-v0",
    ).forEach {
        modImplementation(fabricApi.module(it, "0.48.0+1.18.2"))
    }

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }

    "com.github.llamalad7:mixinextras:0.0.+".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version
            )
        }
    }

    shadowJar {
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set(null as String?)
    }

    jar {
        archiveClassifier.set("dev")
    }
}

components["java"].withGroovyBuilder {
    "withVariantsFromConfiguration"(configurations["shadowRuntimeElements"]) {
        "skip"()
    }
}
