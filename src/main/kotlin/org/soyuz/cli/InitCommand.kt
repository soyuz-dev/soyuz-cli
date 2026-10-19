
package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path
import java.time.Year

class InitCommand : CliktCommand(name = "init") {
    override val invokeWithoutSubcommand = true

    private val yes by option(
        "--yes", "-y",
        help = "Use defaults without prompting"
    ).flag()

    private val name by option(
        "--name",
        help = "Project name"
    )

    private val description by option(
        "--description",
        help = "Project description"
    )

    private val license by option(
        "--license",
        help = "Licence type"
    ).enum<LicenseType>(ignoreCase = true)

    private val holder by option(
        "--holder",
        help = "Copyright holder"
    )

    private val year by option(
        "--year",
        help = "Copyright year"
    ).int()

    private val gitignore by option(
        "--gitignore",
        help = "Gitignore template"
    ).enum<GitignoreType>(ignoreCase = true)

    override fun run() {
        if (currentContext.invokedSubcommand != null) return

        if (!yes) {
            echo("Interactive initialisation is not implemented yet.")
            echo("Use soyuz init --yes or an individual init subcommand.")
            return
        }

        val directory = Path.of("")
            .toAbsolutePath()
            .normalize()

        val projectName = name
            ?: directory.fileName?.toString()
            ?: fail("Cannot determine project name")

        failIf(
            license?.requiresCopyright == true && holder.isNullOrBlank(),
            "Specify --holder for ${license?.spdx}"
        )

        val copyrightYear = year ?: Year.now().value

        failIf(
            copyrightYear !in 1..9999,
            "Invalid copyright year: $copyrightYear"
        )

        val plan = InitPlan(
            directory = directory,
            name = projectName,
            description = description,
            license = license,
            holder = holder,
            year = copyrightYear,
            readme = true,
            gitignore = gitignore?.let { setOf(it) } ?: emptySet(),
        )

        try {
            InitExecutor().execute(plan)
        } catch (e: FileAlreadyExistsException) {
            fail("File already exists: ${e.file}")
        } catch (e: IllegalArgumentException) {
            fail(e.message ?: "Invalid project configuration")
        }

        echo("Project initialised at $directory")
    }
}
