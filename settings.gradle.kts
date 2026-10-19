@Suppress("UNCHECKED_CAST")
try {
    val pe = Class.forName("java.lang.ProcessEnvironment")
    val f1 = pe.getDeclaredField("theEnvironment").apply { isAccessible = true }
    (f1.get(null) as? MutableMap<String, String>)?.remove("ANDROID_PREFS_ROOT")
    val f2 = pe.getDeclaredField("theCaseInsensitiveEnvironment").apply { isAccessible = true }
    (f2.get(null) as? MutableMap<String, String>)?.remove("ANDROID_PREFS_ROOT")
} catch (_: Throwable) {}

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyAppNavigation"
include(":app")
