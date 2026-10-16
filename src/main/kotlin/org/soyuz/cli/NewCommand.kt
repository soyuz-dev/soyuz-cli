package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import java.nio.file.Files
import java.nio.file.Path

class NewCommand: CliktCommand(name = "new") {
    private val type by argument().enum<ProjectType>()
    private val name by argument()
    private val lib by option("--lib", help = "Generate a library instead of an application")
        .flag()

    override fun run() {
        failIf(
            !Regex("[A-Za-z][A-Za-z0-9_-]*").matches(name),
            "Invalid project name: $name"
        )

        val home = Path.of(System.getProperty("user.home"))

        val directory = when(type) {
            ProjectType.NEXT ->  "WebStormProjects"
            ProjectType.ANDROID -> "AndroidStudioProjects"
            else -> "IdeaProjects"
        }

        val destination = home.resolve(directory).resolve(name)

        failIf(Files.exists(destination),"Directory already exists: $destination")

        failIf(
            type != ProjectType.KOTLIN && type != ProjectType.JAVA,
            "Project generation for $type is not implemented yet"
        )

        echo("Creating project $name at $directory")

        try {
            GradleGenerator().generate(type, name, destination, lib)
        } catch (e: Exception) {
            fail("Project creation failed: ${e.message}")
        }

        echo("Created project at $destination")
    }
}