plugins {
    kotlin("jvm") version "2.4.0"
    application
}

group = "org.soyuz"
version = "0.1-alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.ajalt.clikt:clikt:5.1.0")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "org.soyuz.cli.MainKt"
}