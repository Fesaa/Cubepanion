import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
    id("signing")
    id("maven-publish")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")
group = "art.ameliah.laby.addons"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

labyMod {
    defaultPackageName = "art.ameliah.laby.addons.cubepanion"

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client")
            }

            val file = file("./game-runner/src/${this.sourceSetName}/resources/cubepanion-${versionId}.accesswidener")
            accessWidener.set(file)
        }
    }

    addonInfo {
        namespace = "cubepanion"
        displayName = "Cubepanion"
        author = "Amelia"
        description = "A CubeCraft focused LabyMod addon that provides a ton of useful features!\nNot affiliated with Ziax/CubeCraft in any way."
        minecraftVersion = "1.20<*"
        version = rootProject.version.toString()
        releaseChannel = ReleaseChannels.PRODUCTION
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}



