// Plugins
plugins {
    kotlin("jvm") version "2.3.20-RC3"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    `maven-publish`
}

// Config
group = "net.tfj"
version = "1.3.0"
val targetJavaVersion = 21

// Dependencies
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

// Repositories
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

// Java config
java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

// Kotlin config
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

// TasK: process resources
tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

// Shadow configuration
tasks.shadowJar {
    archiveFileName.set("ScoreboardAPI-${project.version}.jar")
}

// RunPaper configuration
tasks.runServer {
    minecraftVersion("1.21.11")
}

// Publishing
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}