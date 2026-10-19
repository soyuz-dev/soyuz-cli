
package org.soyuz.cli

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class InitExecutorTest {
    @TempDir
    lateinit var directory: Path

    private val executor = InitExecutor()

    @Test
    fun `creates a README`() {
        executor.execute(
            InitPlan(
                directory = directory,
                name = "VeKtors",
                description = "A Kotlin linear algebra library",
            )
        )

        val readme = Files.readString(directory.resolve("README.md"))

        assertEquals(
            "# VeKtors\n\nA Kotlin linear algebra library\n",
            readme.replace("\r\n", "\n")
        )
    }

    @Test
    fun `creates a licence`() {
        executor.execute(
            InitPlan(
                directory = directory,
                name = "Example",
                readme = false,
                license = LicenseType.MIT,
                holder = "soyuz-dev",
                year = 2026,
            )
        )

        val license = Files.readString(directory.resolve("LICENSE"))

        assertTrue("2026" in license)
        assertTrue("soyuz-dev" in license)
        assertFalse("{{YEAR}}" in license)
        assertFalse("{{HOLDER}}" in license)
        assertFalse(Files.exists(directory.resolve("README.md")))
    }

    @Test
    fun `combines gitignore templates`() {
        executor.execute(
            InitPlan(
                directory = directory,
                name = "Example",
                readme = false,
                gitignore = setOf(
                    GitignoreType.KOTLIN,
                    GitignoreType.ANDROID,
                )
            )
        )

        val gitignore = Files.readString(directory.resolve(".gitignore"))

        assertTrue(".gradle/" in gitignore)
        assertTrue("local.properties" in gitignore)

        assertEquals(
            1,
            gitignore.lines().count { it == ".gradle/" }
        )
    }

    @Test
    fun `preserves an existing readme`() {
        val path = directory.resolve("README.md")
        Files.writeString(path, "# Existing project\n")

        assertFailsWith<java.nio.file.FileAlreadyExistsException> {
            executor.execute(
                InitPlan(
                    directory = directory,
                    name = "Replacement",
                )
            )
        }

        assertEquals("# Existing project\n", Files.readString(path))
    }

    @Test
    fun `does nothing when all generators are disabled`() {
        executor.execute(
            InitPlan(
                directory = directory,
                name = "Example",
                readme = false,
            )
        )

        Files.list(directory).use {
            assertFalse(it.findAny().isPresent)
        }
    }


    @Test
    fun `does not partially initialise on conflict`() {
        Files.writeString(
            directory.resolve("LICENSE"),
            "Existing licence"
        )

        assertFailsWith<java.nio.file.FileAlreadyExistsException> {
            executor.execute(
                InitPlan(
                    directory = directory,
                    name = "Example",
                    license = LicenseType.MIT,
                    holder = "soyuz-dev",
                    year = 2026,
                )
            )
        }

        assertFalse(Files.exists(directory.resolve("README.md")))
        assertEquals(
            "Existing licence",
            Files.readString(directory.resolve("LICENSE"))
        )
    }

}
