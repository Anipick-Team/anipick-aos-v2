rootProject.name = "AniPick_v2"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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

        // Kakao
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:model")
include(":core:navigation")
include(":core:network")
include(":core:ui")
include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:splash:api")
include(":feature:splash:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:ranking:api")
include(":feature:ranking:impl")
include(":feature:explore:api")
include(":feature:explore:impl")
include(":feature:mypage:api")
include(":feature:mypage:impl")
include(":feature:search:api")
include(":feature:search:impl")
