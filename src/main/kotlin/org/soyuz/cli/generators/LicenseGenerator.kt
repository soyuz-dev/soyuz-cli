package org.soyuz.cli.generators

import org.soyuz.cli.LicenseType
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class LicenseGenerator {
    fun generate(
        directory: Path,
        type: LicenseType,
        holder: String? = null,
        year: Int? = null,
    ) {
        val resource = "licenses/${type.spdx}.txt"

        val template = javaClass.classLoader
            .getResourceAsStream(resource)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: error("Missing licence template: $resource")

        val content = if (type.requiresCopyright) {
            require(!holder.isNullOrBlank()) {
                "Copyright holder is required for ${type.spdx}"
            }
            require(year != null) {
                "Copyright year is required for ${type.spdx}"
            }
            check(
                "{{YEAR}}" in template &&
                        "{{HOLDER}}" in template
            ) {
                "Missing copyright placeholders in $resource"
            }

            template
                .replace("{{YEAR}}", year.toString())
                .replace("{{HOLDER}}", holder)
        } else {
            template
        }

        Files.writeString(
            directory.resolve("LICENSE"),
            content,
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.WRITE,
        )
    }
}
