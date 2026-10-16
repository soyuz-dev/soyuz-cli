package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import org.soyuz.cli.generators.ReadmeGenerator
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path

class ReadmeCommand : CliktCommand(name = "readme") {
    private val name by option(
        "--name",
        help = "Project name"
    )

    private val description by option(
        "--description",
        help = "Project description"
    )

    override fun run() {
        val directory = Path.of("")
            .toAbsolutePath()
            .normalize()

        val projectName = name ?: directory.fileName?.toString()
        ?: fail("Cannot determine project name")

        try {
            ReadmeGenerator().generate(
                directory,
                projectName,
                description,
            )
        } catch (e: FileAlreadyExistsException) {
            fail("README.md already exists")
        }

        echo("Created README.md")
    }
}