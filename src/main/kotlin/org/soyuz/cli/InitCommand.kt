
package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class InitCommand : CliktCommand(name = "init") {
    override val invokeWithoutSubcommand = true

    private val yes by option(
        "--yes", "-y",
        help = "Use default values without prompting"
    ).flag()

    override fun run() {
        if (currentContext.invokedSubcommand != null) return

        if (yes) {
            echo("Initialising project with defaults")
        } else {
            echo("Interactive project initialisation")
        }
    }
}
