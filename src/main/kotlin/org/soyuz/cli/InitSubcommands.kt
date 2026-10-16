
package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.default

class ReadmeCommand : CliktCommand(name = "readme") {
    override fun run() {
        echo("Generating README.md")
    }
}

class GitignoreCommand : CliktCommand(name = "gitignore") {
    override fun run() {
        echo("Generating .gitignore")
    }
}
