package com.jparkbro.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.konan.properties.Properties

internal fun Project.configureApplicationBuildTypes(
    applicationExtension: ApplicationExtension,
    keystoreProperties: Properties
) {
    configureBuildTypes(applicationExtension)

    applicationExtension.apply {
        if (keystoreProperties.isNotEmpty()) {
            signingConfigs {
                create("release") {
                    storeFile = file(keystoreProperties["storeFile"] as String)
                    storePassword = keystoreProperties["storePassword"] as String
                    keyAlias = keystoreProperties["keyAlias"] as String
                    keyPassword = keystoreProperties["keyPassword"] as String
                }
            }

            buildTypes.getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true
                signingConfig = signingConfigs.getByName("release")
            }
        } else {
            buildTypes.getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true
            }
        }
    }
}

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension
) {
    commonExtension.apply {
        buildFeatures.apply {
            buildConfig = true
        }

        val localProperties = gradleLocalProperties(rootDir, providers)
        val webClientId = localProperties.getProperty("WEB_CLIENT_ID") ?: ""
        val kakaoAppKey = localProperties.getProperty("KAKAO_APP_KEY") ?: ""
        val appVersion = libs.findVersion("projectVersionName").get().toString()

        buildTypes {
            getByName("debug") {
                configureDebugBuildType(appVersion, webClientId, kakaoAppKey)
            }
            getByName("release") {
                configureReleaseBuildType(commonExtension, appVersion, webClientId, kakaoAppKey)
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    appVersion: String,
    webClientId: String,
    kakaoAppKey: String,
) {
    buildConfigField("String", "APP_VERSION", "\"$appVersion\"")
    buildConfigField("String", "WEB_CLIENT_ID", "\"$webClientId\"")
    buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
    buildConfigField("String", "BASE_URL", "\"https://anipick.p-e.kr/api/\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension,
    appVersion: String,
    webClientId: String,
    kakaoAppKey: String,
) {
    buildConfigField("String", "APP_VERSION", "\"$appVersion\"")
    buildConfigField("String", "WEB_CLIENT_ID", "\"$webClientId\"")
    buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
    buildConfigField("String", "BASE_URL", "\"https://anipick.p-e.kr/api/\"")

    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt")
    )
}