package org.soyuz.cli.generators

import java.nio.file.Path

class AndroidGenerator {
    fun generate(
        name: String,
        destination: Path,
    ) {
        val packageName = name
            .lowercase()
            .replace(Regex("[^a-z0-9_]"), "")

        require(packageName.matches(Regex("[a-z_][a-z0-9_]*"))) {
            "Invalid Android package name: $name"
        }

        val namespace = "org.soyuz.$packageName"

        val command = listOf(
            "cmd.exe", "/d", "/c",
            "android",
            "create",
            "--name=$name",
            "--namespace=$namespace",
            "--application-id=$namespace",
            "--min-sdk=29",
            "--output=$destination",
        )

        val process = ProcessBuilder(command)
            .inheritIO()
            .start()

        check(process.waitFor() == 0) {
            "Android project generation failed"
        }
    }
}
