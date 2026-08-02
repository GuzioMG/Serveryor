import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
	id("net.fabricmc.fabric-loom-remap")
	`maven-publish`
	id("org.jetbrains.kotlin.jvm") version "2.4.10"
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

repositories {
	// Default docs:
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.

	// Surveyor Repo:
	maven { url = URI("https://repo.sleeping.town/") }

	// Other repositories can go above or below Modrinth's. We don't need priority :)
	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = URI("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

dependencies {
	// To change the versions see the gradle.properties file

	//Core:
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API. This is technically optional, but you probably want it anyway:
	modImplementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

	//Kotlin:
	modImplementation("net.fabricmc:fabric-language-kotlin:${providers.gradleProperty("fabric_kotlin_version").get()}")

	//Surveyor:
	modImplementation("folk.sisby:surveyor:1.2.4+1.21")

	//KTor:
	implementation(ktorLibs.server.core)
	implementation(ktorLibs.server.jetty)

	//Extra things to auto-download by Fabric Loom; added here for convenience during dev (I need a map to compare to, and Mod Menu to see if I have all my metadata in check):
	modImplementation("maven.modrinth:hoofprint:1.3.0+1.21")
	modImplementation("maven.modrinth:modmenu:11.0.3")
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_21
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}