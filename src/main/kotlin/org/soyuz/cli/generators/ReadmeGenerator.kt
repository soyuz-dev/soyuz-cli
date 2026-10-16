package org.soyuz.cli.generators

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class ReadmeGenerator {
    fun generate(
        directory: Path,
        name: String,
        description: String? = null,
    ) {
        val content = buildString {
            appendLine("# $name")

            if (!description.isNullOrBlank()) {
                appendLine()
                appendLine(description)
            }
        }

        Files.writeString(
            directory.resolve("README.md"),
            content,
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.WRITE,
        )
    }
}