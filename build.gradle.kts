plugins {
    // Kotlin 2.4 is the consumer floor for bmc4j 0.4.x (its plugin/KGP uses 2.4-era compiler APIs).
    kotlin("jvm") version "2.4.0"
    // bmc4j applies `java` + JUnit 5 and wires the proof runtime + the bundled engine, so a `@BmcProof`
    // is just a JUnit 5 test. The plugin analyzes the SHIPPED bytecode of this project's `main`
    // sources — i.e. the vendored TheAlgorithms/Kotlin functions exactly as kotlinc compiles them.
    // Version is pinned in settings.gradle.kts `pluginManagement` (override via -PbmcVersion).
    id("org.bmc4j")
}

kotlin {
    jvmToolchain(21)
}
