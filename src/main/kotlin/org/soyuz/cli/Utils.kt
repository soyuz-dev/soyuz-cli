package org.soyuz.cli

import com.github.ajalt.clikt.core.UsageError

fun fail(message: String): Nothing = throw UsageError(message)

fun failIf(condition: Boolean, message: String) {
    if (condition) fail(message)
}