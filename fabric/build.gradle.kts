plugins {
    id("com.github.johnrengelman.shadow") version "7.+"
    id("com.modrinth.minotaur")
    id("com.matthewprenger.cursegradle")
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
    val clothVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    listOf(
        "fabric-lifecycle-events-v1",
        "fabric-resource-loader-v0",
    ).forEach {
        modImplementation(fabricApi.module(it, "0.48.0+1.18.2"))
    }

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:$clothVersion") {
        exclude(module = "fabric-api")
    }
    modImplementation("com.terraformersmc:modmenu:4.+")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }
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

val minecraftVersion: String by rootProject

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("EROYCQAh")
    versionName.set("[${project.name.capitalize()} $minecraftVersion] ${project.version}")
    versionNumber.set("${project.version}-${project.name}")
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(listOf(project.name))

    dependencies.addAll(
        com.modrinth.minotaur.dependencies.Dependency("fabric-api", com.modrinth.minotaur.dependencies.DependencyType.REQUIRED),
        com.modrinth.minotaur.dependencies.Dependency("modmenu", com.modrinth.minotaur.dependencies.DependencyType.OPTIONAL)
    )
}

rootProject.tasks["publishToModrinth"].dependsOn(tasks["modrinth"])

if (hasProperty("curseforge.token")) {
    curseforge {
        apiKey = findProperty("curseforge.token")
        project(closureOf<com.matthewprenger.cursegradle.CurseProject> {
            mainArtifact(tasks.remapJar.get(), closureOf<com.matthewprenger.cursegradle.CurseArtifact> {
                displayName = "[${project.name.capitalize()} $minecraftVersion] ${project.version}"

                relations(closureOf<com.matthewprenger.cursegradle.CurseRelation> {
                    requiredDependency("fabric-api")
                    requiredDependency("cloth-config")
                    optionalDependency("modmenu")
                })
            })

            id = "599215"
            releaseType = "release"
            addGameVersion(minecraftVersion)
            addGameVersion(project.name.capitalize())
            addGameVersion("Java 17")
        })

        options(closureOf<com.matthewprenger.cursegradle.Options> {
            forgeGradleIntegration = false
        })
    }
}

rootProject.tasks["publishToCurseforge"].dependsOn(tasks["curseforge"])
