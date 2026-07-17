plugins {
    alias(libs.plugins.anipick.android.library.compose)
}

android {
    namespace = "com.jparkbro.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.bundles.navigation3)
}