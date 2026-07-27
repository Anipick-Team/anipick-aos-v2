plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.auth.impl"
}

dependencies {
    implementation(projects.feature.auth.api)
    implementation(projects.feature.home.api)
}