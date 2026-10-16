package org.soyuz.cli

enum class LicenseType(val spdx: String) {
    MPL2("MPL-2.0"),
    GPL3("GPL-3.0"),
    LGPL3("LGPL-3.0"),
    UNLICENSE("Unlicense"),
    MIT("MIT"),
    APACHE2("Apache-2.0"),
    BSD2("BSD-2-Clause"),
    BSD3("BSD-3-Clause"),
}