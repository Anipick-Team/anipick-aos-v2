plugins {
    alias(libs.plugins.anipick.android.application.compose)
}

android {
    namespace = "com.jparkbro.anipick"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Oss Licenses
    implementation(libs.ui.compose.material3)

    // In App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // Kakao (KakaoSdk.init 호출용)
    implementation(libs.kakao.user)

    // Module
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.ui)

    implementation(projects.feature.splash.api)
    implementation(projects.feature.splash.impl)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.auth.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.ranking.api)
    implementation(projects.feature.ranking.impl)
    implementation(projects.feature.explore.api)
    implementation(projects.feature.explore.impl)
    implementation(projects.feature.mypage.api)
    implementation(projects.feature.mypage.impl)
}