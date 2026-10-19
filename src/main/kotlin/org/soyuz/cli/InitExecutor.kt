package org.soyuz.cli

import org.soyuz.cli.generators.GitignoreGenerator
import org.soyuz.cli.generators.LicenseGenerator
import org.soyuz.cli.generators.ReadmeGenerator
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files

class InitExecutor {
    fun execute(plan: InitPlan) {
        val readme = plan.directory.resolve("README.md")
        val license = plan.directory.resolve("LICENSE")

        if (plan.readme && Files.exists(readme)) {
            throw FileAlreadyExistsException(readme.toString())
        }

        if (plan.license != null && Files.exists(license)) {
            throw FileAlreadyExistsException(license.toString())
        }

        if (plan.readme) {
            ReadmeGenerator().generate(
                plan.directory,
                plan.name,
                plan.description,
            )
        }

        plan.license?.let {
            LicenseGenerator().generate(
                plan.directory,
                it,
                plan.holder,
                plan.year,
            )
        }

        plan.gitignore.forEach {
            GitignoreGenerator().generate(plan.directory, it)
        }
    }
}