package org.soyuz.cli

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object GradleBootstrap {
    private val home = Path.of(
        System.getProperty("user.home"),
        ".soyuz",
        "gradle-bootstrap"
    )

    private val files = listOf(
        "gradlew.bat",
        "gradle/wrapper/gradle-wrapper.jar",
        "gradle/wrapper/gradle-wrapper.properties"
    )

    fun executable(): Path {
        for (file in files) {
            val destination = home.resolve(file)
            Files.createDirectories(destination.parent)

            val resource = javaClass.classLoader
                .getResourceAsStream("gradle-bootstrap/$file")
                ?: error("Missing bundled Gradle resource: $file")

            resource.use {
                Files.copy(
                    it,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        return home.resolve("gradlew.bat")
    }
}