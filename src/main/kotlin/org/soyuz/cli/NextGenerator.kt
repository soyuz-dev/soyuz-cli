
package org.soyuz.cli

import java.nio.file.Path

class NextGenerator {
    fun generate(destination: Path) {
        val command = listOf(
            "npx.exe",
            "--yes",
            "create-next-app@latest",
            destination.toString(),
            "--typescript",
            "--tailwind",
            "--eslint",
            "--app",
            "--src-dir",
            "--empty",
            "--use-npm",
            "--yes"
        )

        val process = ProcessBuilder(command)
            .inheritIO()
            .start()

        val exitCode = process.waitFor()

        check(exitCode == 0) {
            "Next.js generation failed (exit code $exitCode)"
        }
    }
}
