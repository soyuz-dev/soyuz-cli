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
    jvmToolchain(21)
}


tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

tasks.processResources {
    from("gradlew.bat") {
        into("gradle-bootstrap")
    }

    from("gradle/wrapper") {
        into("gradle-bootstrap/gradle/wrapper")
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "org.soyuz.cli.MainKt"
    applicationName = "soyuz"
}