package org.soyuz.cli

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    Soyuz()
        .subcommands(
            NewCommand(),
            InitCommand().subcommands(
                ReadmeCommand(),
                LicenseCommand(),
                GitignoreCommand()
            )
        )
        .main(args)
}