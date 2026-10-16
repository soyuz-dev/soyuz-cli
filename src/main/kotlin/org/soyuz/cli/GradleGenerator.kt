package org.soyuz.cli

import java.nio.file.Files
import java.nio.file.Path

class GradleGenerator {
    fun generate(
        type: ProjectType,
        name: String,
        destination: Path,
        lib: Boolean = false,
    ) {
        val gradleType = when (type) {
            ProjectType.KOTLIN -> if (lib) "kotlin-library" else "kotlin-application"
            ProjectType.JAVA -> if(lib) "java-library" else "java-application"
            else -> error("Unsupported Gradle project type: $type")
        }

        val gradle = GradleBootstrap.executable()

        Files.createDirectories(destination)

        val command = listOf(
            "cmd.exe", "/c", gradle.toString(),
            "init",
            "--type", gradleType,
            "--dsl", "kotlin",
            "--project-name", name,
            "--package", "org.soyuz",
            "--no-split-project",
            "--use-defaults",
            "--no-incubating",
        )

        val process = ProcessBuilder(command)
            .directory(destination.toFile())
            .inheritIO()
            .start()

        val exitCode = process.waitFor()

        check(exitCode == 0) {
            "Gradle project generation failed (exit code $exitCode)"
        }
    }
}