package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import java.nio.file.Files
import java.nio.file.Path

class NewCommand: CliktCommand(name = "new") {
    private val type by argument().enum<ProjectType>()
    private val target by argument().optional()
    private val lib by option("--lib", help = "Generate a library instead of an application")
        .flag()

    override fun run() {
        val destination = resolveDestination(type, target)
        val name = destination.fileName?.toString()
            ?: fail("Cannot determine project name")

        failIf(
            Files.exists(destination) &&
                    (!Files.isDirectory(destination) ||
                            Files.list(destination).use { it.findAny().isPresent }),
            "Destination is not an empty directory: $destination"
        )

        failIf(
            type != ProjectType.KOTLIN && type != ProjectType.JAVA,
            "Unsupported project type: $type"
        )

        echo("Creating project $name at $destination")

        try {
            GradleGenerator().generate(type, name, destination, lib)
        } catch (e: Exception) {
            fail("Project creation failed: ${e.message}")
        }

        echo("Created project at $destination")
    }

    private fun resolveDestination(
        type: ProjectType,
        target: String?,
    ): Path {
        val cwd = Path.of("").toAbsolutePath().normalize()
        val home = Path.of(System.getProperty("user.home"))

        if (target == null || target == ".") return cwd

        val path = Path.of(target)

        if (path.isAbsolute || path.nameCount > 1 ||
            target.startsWith(".") || target.contains('\\') || target.contains('/')
        ) {
            return cwd.resolve(path).normalize()
        }

        val directory = when (type) {
            ProjectType.NEXT -> "WebStormProjects"
            ProjectType.ANDROID -> "AndroidStudioProjects"
            else -> "IdeaProjects"
        }

        return home.resolve(directory).resolve(target).normalize()
    }
}