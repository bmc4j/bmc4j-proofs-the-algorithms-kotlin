// Repositories: GitHub Packages is bmc4j's pre-Central snapshot channel — it needs SOME authenticated
// token with read:packages even for public packages (in CI the workflow's own GITHUB_TOKEN; locally
// `gh auth token` if it has the scope, or -Pgpr.user/-Pgpr.token). mavenLocal first so a local
// `publishToMavenLocal` of bmc4j also resolves (used by the `-PbmcVersion=0.0.1-local` fast loop).
pluginManagement {
    repositories {
        mavenLocal()
        maven {
            name = "Bmc4jGitHubPackages"
            url = uri("https://maven.pkg.github.com/bmc4j/bmc4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN") ?: providers.gradleProperty("gpr.token").orNull
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
    // bmc4j plugin version: CI uses the published GitHub Packages SNAPSHOT (default); the local fast
    // loop overrides with `-PbmcVersion=0.0.1-local` to consume a `publishToMavenLocal` build.
    //   PowerShell:  ./gradlew --% test -PbmcVersion=0.0.1-local
    plugins {
        val bmcVersion = (settings.providers.gradleProperty("bmcVersion").orNull) ?: "0.4.4-b2f3a9d"
        id("org.bmc4j") version bmcVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            name = "Bmc4jGitHubPackages"
            url = uri("https://maven.pkg.github.com/bmc4j/bmc4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN") ?: providers.gradleProperty("gpr.token").orNull
            }
        }
        mavenCentral()
    }
}

rootProject.name = "bmc4j-proofs-the-algorithms-kotlin"
