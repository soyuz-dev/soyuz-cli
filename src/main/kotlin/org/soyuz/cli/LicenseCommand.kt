
package org.soyuz.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import org.soyuz.cli.generators.LicenseGenerator
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path
import java.time.Year

class LicenseCommand : CliktCommand(name = "license") {
    private val type by option(
        "--type",
        help = "Licence type"
    ).enum<LicenseType>(ignoreCase = true).required()

    private val holder by option(
        "--holder",
        help = "Copyright holder"
    )

    private val year by option(
        "--year",
        help = "Copyright year"
    ).int().default(Year.now().value)

    override fun run() {
        val directory = Path.of("")
            .toAbsolutePath()
            .normalize()

        failIf(
            type.requiresCopyright && holder.isNullOrBlank(),
            "Specify --holder for ${type.spdx}"
        )

        failIf(
            year !in 1..9999,
            "Invalid copyright year: $year"
        )

        try {
            LicenseGenerator().generate(
                directory,
                type,
                holder,
                year,
            )
        } catch (e: FileAlreadyExistsException) {
            fail("LICENSE already exists")
        }

        echo("Created LICENSE (${type.spdx})")
    }
}
