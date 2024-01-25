import java.util.function.Supplier

plugins {
    id("java-library")
    id("net.labymod.gradle")
    id("net.labymod.gradle.addon")
    id("signing")
    id("maven-publish")
}

group = "art.ameliah.laby.addons"
version = "2.0.6"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

labyMod {
    defaultPackageName = "art.ameliah.laby.addons.cubepanion" //change this to your main package name (used by all modules)
    addonInfo {
        namespace = "cubepanion"
        displayName = "Cubepanion"
        author = "Fesa"
        description = "A CubeCraft focused LabyMod addon that provides a ton of useful features!\nNot affiliated with Ziax/CubeCraft in any way."
        minecraftVersion = "1.19.4<*"
        version = System.getenv().getOrDefault("VERSION", "0.0.1")
    }

    minecraft {
        registerVersions(
                "1.19.4",
                "1.20.1",
                "1.20.2",
                "1.20.4",
        ) { version, provider ->
            configureRun(provider, version)

            provider.accessWidener = Supplier {
                val sourceSetName = version.replace(".", "_").replace("-", "_")
                file("./game-runner/src/v$sourceSetName/resources/cubepanion-$version.accesswidener")
            }
        }

        subprojects.forEach {
            if (it.name != "game-runner") {
                filter(it.name)
            }
        }
    }

    addonDev {
        productionRelease()
    }
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("net.labymod.gradle")
    plugins.apply("net.labymod.gradle.addon")

    repositories {
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    val shade = configurations.create("shade")
    val api by configurations
    api.extendsFrom(shade)
}

fun configureRun(provider: net.labymod.gradle.core.minecraft.provider.VersionProvider, gameVersion: String) {
    provider.runConfiguration {
        mainClass = "net.minecraft.launchwrapper.Launch"
        jvmArgs("-Dnet.labymod.running-version=${gameVersion}")
        jvmArgs("-Dmixin.debug=true")
        jvmArgs("-Dnet.labymod.debugging.all=true")
        jvmArgs("-Dmixin.env.disableRefMap=true")

        if (org.gradle.internal.os.OperatingSystem.current() == org.gradle.internal.os.OperatingSystem.MAC_OS) {
            jvmArgs("-XstartOnFirstThread")
        }

        args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.Java17LabyModLaunchWrapperTweaker")
        args("--labymod-dev-environment", "true")
        args("--addon-dev-environment", "true")
    }

    provider.javaVersion = JavaVersion.VERSION_17

    provider.mixin {
        val mixinMinVersion = when (gameVersion) {
            "1.8.9", "1.12.2", "1.16.5" -> {
                "0.6.6"
            }

            else -> {
                "0.8.2"
            }
        }

        minVersion = mixinMinVersion
    }
}

configurations {
    create("conf")
}
val rFile = layout.buildDirectory.file("libs//cubepanion-release.jar")
val rArtifact = artifacts.add("conf", rFile.get().asFile) {
    type = "jar"
    builtBy("build")
}



publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "cubepanion"
            artifact(rArtifact)

            pom {
                name.set("cubepanion")
                description.set("CubeCraft Laby Addon")
                url.set("https://github.com/Fesaa/Cubepanion")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://mit-license.org/")
                    }
                }

                developers {
                    developer {
                        id.set("amelia")
                        name.set("Amelia")
                    }
                }
            }
        }
    }
}

