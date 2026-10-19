package org.soyuz.cli

import java.nio.file.Path

data class InitPlan(
    val directory: Path,
    val name: String,
    val description: String? = null,
    val license: LicenseType? = null,
    val holder: String? = null,
    val year: Int? = null,
    val readme: Boolean = true,
    val gitignore: Set<GitignoreType> = emptySet(),
)