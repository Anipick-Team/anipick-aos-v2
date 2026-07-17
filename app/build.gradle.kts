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

    // Module
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.ui)
}