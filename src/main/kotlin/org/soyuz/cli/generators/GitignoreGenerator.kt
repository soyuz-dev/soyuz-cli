
package org.soyuz.cli.generators

import org.soyuz.cli.GitignoreType
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class GitignoreGenerator {
    fun generate(
        directory: Path,
        type: GitignoreType,
    ): Int {
        val destination = directory.resolve(".gitignore")
        val resource = "gitignore/${type.name.lowercase().replaceFirstChar { it.uppercase() }}.txt"

        val template = javaClass.classLoader
            .getResourceAsStream(resource)
            ?.bufferedReader()
            ?.use { it.readLines() }
            ?: error("Missing gitignore template: $resource")

        val existing = if (Files.exists(destination)) {
            Files.readAllLines(destination)
        } else {
            emptyList()
        }

        val existingRules = existing
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .toSet()

        val missing = template
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .distinct()
            .filterNot { it in existingRules }

        if (missing.isEmpty()) return 0

        val content = buildString {
            if (existing.isNotEmpty()) {
                if (existing.last().isNotBlank()) appendLine()
                appendLine()
            }

            appendLine("# Soyuz: ${type.name.lowercase()}")
            missing.forEach { appendLine(it) }
        }

        Files.writeString(
            destination,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND,
        )

        return missing.size
    }
}
