import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.jparkbro.anipick.buildlogic"

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.anipick.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.anipick.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.anipick.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.anipick.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureApi") {
            id = libs.plugins.anipick.android.feature.api.get().pluginId
            implementationClass = "AndroidFeatureApiConventionPlugin"
        }
        register("androidFeatureImpl") {
            id = libs.plugins.anipick.android.feature.impl.get().pluginId
            implementationClass = "AndroidFeatureImplConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.anipick.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmKtor") {
            id = libs.plugins.anipick.jvm.ktor.get().pluginId
            implementationClass = "JvmKtorConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.anipick.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}