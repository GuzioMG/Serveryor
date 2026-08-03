pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}

	plugins {
		id("net.fabricmc.fabric-loom-remap") version providers.gradleProperty("loom_version")
	}
}

dependencyResolutionManagement {
	repositories {
		mavenCentral()
	}
	versionCatalogs {
		create("ktorLibs").from("io.ktor:ktor-version-catalog:3.5.0")
	}
}


// Should match your modid
rootProject.name = "surwebyor"