
package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import org.soyuz.cli.generators.GitignoreGenerator
import java.nio.file.Path

class GitignoreCommand : CliktCommand(name = "gitignore") {
    private val type by option(
        "--type",
        help = "Project type"
    ).enum<GitignoreType>(ignoreCase = true).required()

    override fun run() {
        val directory = Path.of("")
            .toAbsolutePath()
            .normalize()

        val added = GitignoreGenerator().generate(directory, type)

        if (added == 0) {
            echo(".gitignore is already up to date")
        } else {
            echo("Added $added rules to .gitignore")
        }
    }
}
