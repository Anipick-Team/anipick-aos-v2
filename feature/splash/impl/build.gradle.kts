plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.splash.impl"
}

dependencies {
    implementation(projects.feature.splash.api)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.home.api)
}
